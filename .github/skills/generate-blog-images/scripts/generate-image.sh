#!/usr/bin/env bash
set +x
set -euo pipefail

usage() {
  cat <<'USAGE'
Generate an image with the OpenAI Images API.

Usage:
  generate-image.sh --prompt TEXT --output PATH [--size SIZE] [--model MODEL] [--quality QUALITY] [--style STYLE]

Options:
  --prompt TEXT       Required. Natural-language image prompt.
  --output PATH       Required. Path where the generated image will be saved.
  --size SIZE         Optional. Defaults to 1792x1024 for wide blog imagery.
  --model MODEL       Optional. Defaults to dall-e-3.
  --quality QUALITY   Optional. Defaults to standard.
  --style STYLE       Optional. Only sent when specified.
  --help              Show this help text.

Authentication:
  Set OPENAI_API_KEY in the environment. Do not paste the key into prompts,
  command arguments, files, logs, or chat.
USAGE
}

fail() {
  printf 'Error: %s\n' "$1" >&2
  exit 1
}

require_command() {
  command -v "$1" >/dev/null 2>&1 || fail "Missing required command: $1"
}

prompt=""
output=""
size="1792x1024"
model="dall-e-3"
quality="standard"
style=""

while [[ $# -gt 0 ]]; do
  case "$1" in
    --prompt)
      [[ $# -ge 2 ]] || fail "--prompt requires a value"
      prompt="$2"
      shift 2
      ;;
    --output)
      [[ $# -ge 2 ]] || fail "--output requires a value"
      output="$2"
      shift 2
      ;;
    --size)
      [[ $# -ge 2 ]] || fail "--size requires a value"
      size="$2"
      shift 2
      ;;
    --model)
      [[ $# -ge 2 ]] || fail "--model requires a value"
      model="$2"
      shift 2
      ;;
    --quality)
      [[ $# -ge 2 ]] || fail "--quality requires a value"
      quality="$2"
      shift 2
      ;;
    --style)
      [[ $# -ge 2 ]] || fail "--style requires a value"
      style="$2"
      shift 2
      ;;
    --help|-h)
      usage
      exit 0
      ;;
    *)
      fail "Unknown argument: $1"
      ;;
  esac
done

[[ -n "$prompt" ]] || fail "Missing required --prompt"
[[ -n "$output" ]] || fail "Missing required --output"
[[ -n "${OPENAI_API_KEY:-}" ]] || fail "OPENAI_API_KEY is not set. Configure the environment variable on this VPS before generating images."

require_command curl
require_command jq
require_command base64

output_dir="$(dirname "$output")"
mkdir -p "$output_dir"

request_file="$(mktemp)"
response_file="$(mktemp)"
cleanup() {
  rm -f "$request_file" "$response_file"
}
trap cleanup EXIT

jq -n \
  --arg model "$model" \
  --arg prompt "$prompt" \
  --arg size "$size" \
  --arg quality "$quality" \
  --arg style "$style" \
  '(
  {
    model: $model,
    prompt: $prompt,
    n: 1,
    size: $size,
    quality: $quality
  }
  + if $style == "" then {} else {style: $style} end
  )' > "$request_file"

http_code="$(
  {
    printf '%s\n' "header = \"Authorization: Bearer ${OPENAI_API_KEY}\""
    printf '%s\n' 'header = "Content-Type: application/json"'
  } | curl --silent --show-error \
    --request POST "https://api.openai.com/v1/images/generations" \
    --config - \
    --data-binary "@$request_file" \
    --output "$response_file" \
    --write-out "%{http_code}"
)"

if [[ "$http_code" -lt 200 || "$http_code" -ge 300 ]]; then
  message="$(jq -r '.error.message // .message // "OpenAI Images API request failed"' "$response_file" 2>/dev/null || printf 'OpenAI Images API request failed')"
  fail "OpenAI Images API returned HTTP ${http_code}: ${message}"
fi

b64_image="$(jq -r '.data[0].b64_json // empty' "$response_file")"
image_url="$(jq -r '.data[0].url // empty' "$response_file")"

if [[ -n "$b64_image" ]]; then
  printf '%s' "$b64_image" | base64 --decode > "$output"
elif [[ -n "$image_url" ]]; then
  curl --silent --show-error --location "$image_url" --output "$output"
else
  fail "OpenAI Images API response did not include image data or an image URL"
fi

[[ -s "$output" ]] || fail "Image generation completed but no image was written to $output"

printf 'Generated image: %s\n' "$output"

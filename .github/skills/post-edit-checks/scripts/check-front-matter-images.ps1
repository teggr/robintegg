# Script to check that images referenced in front matter exist in the static images folder
# Usage: Run from repository root directory

$contentFiles = Get-ChildItem -Path "website\_posts","website\_podcasts" -Filter "*.md" -Recurse -ErrorAction SilentlyContinue
$imageDir = "website\_static\images"
$missingImages = @()
$validImages = @()

foreach ($file in $contentFiles) {
    $content = Get-Content -Path $file.FullName -Raw
    
    # Extract front matter (between first two --- markers)
    if ($content -match '(?s)^---\s*\n(.*?)\n---') {
        $frontMatter = $matches[1]
        
        # Look for image: field
        if ($frontMatter -match 'image:\s*(.+)') {
            $imagePath = $matches[1].Trim()
            
            # Remove /images/ prefix if present and get just the filename
            $imageFile = $imagePath -replace '^/images/', ''
            $imageFile = $imageFile -replace '^\{\{site\.baseurl\}\}/images/', ''
            
            # Check if image exists
            $fullImagePath = Join-Path $imageDir $imageFile
            
            if (Test-Path $fullImagePath) {
                $validImages += [PSCustomObject]@{
                    ContentFile = $file.Name
                    ImageReference = $imagePath
                    Status = "✓ Exists"
                }
            } else {
                $missingImages += [PSCustomObject]@{
                    ContentFile = $file.Name
                    ImageReference = $imagePath
                    ExpectedPath = $fullImagePath
                    Status = "✗ Missing"
                }
            }
        }
    }
}

Write-Host "`n=== Image Reference Check Results ===" -ForegroundColor Cyan
Write-Host "Total content files scanned: $($contentFiles.Count)" -ForegroundColor White
Write-Host "Files with image references: $($validImages.Count + $missingImages.Count)" -ForegroundColor White
Write-Host ""

if ($missingImages.Count -gt 0) {
    Write-Host "⚠️  MISSING IMAGES ($($missingImages.Count)):" -ForegroundColor Red
    $missingImages | Format-Table -AutoSize
} else {
    Write-Host "✓ All referenced images exist!" -ForegroundColor Green
}

Write-Host "`nValid image references: $($validImages.Count)" -ForegroundColor Green

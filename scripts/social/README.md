# Social publishing

`Social.java` is a JBang script that announces new articles on Mastodon, Bluesky, LinkedIn and X.

1. **Draft**: after each deploy, `build-and-deploy.yml` compares the live feed with the new one. For each new article the LLM writes one text per platform into `scripts/social/queue/<slug>.md` and a pull request is opened.
2. **Review**: edit the text in the PR, or set a platform to `skipped`. Merging the PR is the approval.
3. **Publish**: `social-publish.yml` runs on merge, posts every platform marked `ready` and commits `published` back.

Social drafts use OpenAI and require the `OPENAI_API_KEY` repository Actions secret. The repository setting "Allow GitHub Actions to create pull requests" must be enabled.

To draft a missed article, run **Draft social posts manually** from the Actions tab with its published URL. The workflow opens a review PR; it does not publish directly. An existing queue entry is not overwritten.

Publishing secrets: `MASTODON_BASE_URL`, `MASTODON_TOKEN`, `BLUESKY_HANDLE`, `BLUESKY_APP_PASSWORD`, `LINKEDIN_TOKEN`, `LINKEDIN_AUTHOR_URN`, `X_API_KEY`, `X_API_SECRET`, `X_ACCESS_TOKEN`, `X_ACCESS_SECRET`. A platform without credentials is printed in the job log so it can be posted by hand.

Local test: `jbang scripts/social/Social.java publish --dry-run`

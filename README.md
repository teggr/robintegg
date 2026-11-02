# robintegg

Personal site builder for Robin Tegg

https://www.robintegg.com

Uses [electrostatic](https://github.com/teggr/electrostatic) for building and deploys to GitHub pages.

## Local Development

```
jbang --fresh io.github.teggr:electrostatic-cli:0.0.6 build --base-url=http://localhost:8080
jbang --fresh io.github.teggr:electrostatic-cli:0.0.6 serve --base-url=http://localhost:8080
```

## Publishing

```
jbang --fresh io.github.teggr:electrostatic-cli:0.0.6 build
```

### PostHog analytics

The deployment workflow reads the PostHog browser project key from the repository
Actions **secret** named `POSTHOG_KEY`, not an Actions variable. The build fails if
the secret is missing or empty, rather than deploying without analytics.

Electrostatic embeds the PostHog script when `POSTHOG_KEY` is set in the build
environment. Local builds can omit it to disable analytics. The default API host
is `https://eu.i.posthog.com`.
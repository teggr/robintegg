# robintegg

Personal site builder for Robin Tegg

https://www.robintegg.com

Uses [electrostatic](https://github.com/teggr/electrostatic) for building and deploys to GitHub pages.

## Local Development

```
jbang --fresh run.electrostatic:electrostatic-cli:0.0.1-SNAPSHOT build --base-url=http://localhost:8080
jbang --fresh run.electrostatic:electrostatic-cli:0.0.1-SNAPSHOT serve --base-url=http://localhost:8080
```

## Publishing

```
jbang --fresh run.electrostatic:electrostatic-cli:0.0.1-SNAPSHOT build
```
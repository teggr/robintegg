# robintegg

Personal site builder for Robin Tegg

https://www.robintegg.com

Uses [electrostatic](https://github.com/teggr/electrostatic) for building and deploys to GitHub pages.

## Local Development

```
jbang --fresh site.electrostatic:electrostatic-cli:0.0.3 build --base-url=http://localhost:8080
jbang --fresh site.electrostatic:electrostatic-cli:0.0.3 serve --base-url=http://localhost:8080
```

## Publishing

```
jbang --fresh site.electrostatic:electrostatic-cli:0.0.3 build
```
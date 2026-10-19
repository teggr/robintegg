# robintegg

Personal site builder for Robin Tegg

https://www.robintegg.com

Uses [electrostatic](https://github.com/teggr/electrostatic) for building and deploys to GitHub pages.

## Local Development

```
jbang --fresh io.github.teggr:electrostatic-cli:0.0.4 build --base-url=http://localhost:8080
jbang --fresh io.github.teggr:electrostatic-cli:0.0.4 serve --base-url=http://localhost:8080
```

## Publishing

```
jbang --fresh io.github.teggr:electrostatic-cli:0.0.4 build
```
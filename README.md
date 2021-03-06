jobcoin-mixer
===
JobCoin mixer is a PoorMan's analogy to [bitcoin-mixer](https://mycryptomixer.com/)

### Desciption
A Scala multi project in development

### Requirements
See [notes.org](./notes.org) for detail

### Usage

``` sh
sbt compile
sbt bootstrap/run
```

### Integration tests
Integration tests are in [Spec.http](./scripts/Spec.http)
User [emacs Rest client](https://github.com/pashky/restclient.el) to run the tests, or translate to curl scripts.

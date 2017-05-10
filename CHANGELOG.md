# Change Log

All notable changes to this project will be documented in this file. We try to adhere to https://github.com/olivierlacan/keep-a-changelog.

## 2.2.5.1

- Provide a `DefaultHttpProtocolBuilder` to avoid boiler-plate code
- Removing the `wiremock` configuration since it is not used

## 2.2.5.0

### Changed
- Upgraded to Gatling 2.2.5

### Fixed
- Stop the Gatling JVM when running tests from the Windows command line

### Internal
- Avoid parsing the JSON response but use `bodyString` instead
- Print the currently invoked URL when using the default logging configuration
- Add a `default` and `trace` logging level
- Make `VERSION.txt` and `CHANGELOG.md` available in the Gatling stand-alone distribution
- Add error details to Gatling's `simulation.log`  using an `ExtraInfoExtractor`
- Extracted HttpProtocolBuilder to simplify test setup
- Using `bodyString` instead of `jsonPath` to avoid parsing JSON if not required
- Added a `before` and `after` hook to document the feature 

# Change Log

All notable changes to this project will be documented in this file. We try to adhere to https://github.com/olivierlacan/keep-a-changelog.

## 3.1.2

# Added

- Dockerfile

# Changed

- Upgrade to Gatling 3.1.2

# Fixed

- Add missing dependency for standalone distribution (Boon)

## 3.1.1

### Changed
- Upgrade to Gatling 3.1.1 and Scala Maven Plugin 3.4.6

### Fixed
- Gatling Standalone Distribution lacked "boon-reflekt" library

## 3.0.3

### Changed
- Upgrad to Gatling 3.0.3 and Gatling Maven Plugin 3.0.2

## 3.0.2

### Changed
- Upgrade to Gatling 3.0.2

## 3.0.1.1-SNAPSHOT

### Added 
- Added `pom-minimal.xml` 
- Added `run-samples.sh` to execute the sample tests

### Changed
- Removed explicit `SL4FJ` dependency since this was a bugifx vor Gatling 2.3
- Updated original Gatling example code
- Moved `user-files/resources`to `src/test/resources` to align with Gatling Maven archetype
- Upgraded to Gatling 3.0.1.1 and Gatling Maven Plugin 3.0.0

## 2.3.1

### Added
- Add proof of concept for Elastic performance testing

### Changed
- Upgrade to Gatling 2.3.1
- Update the logback configuration files to remove warning regarding `immediateFlush`

# Fixed
- Get rid of logback warning regarding "immediateFlush"

## 2.3.0

### Added
- Add the official computer database tests

### Changed
- Upgrade to Gatling 2.3.0 - please update your Scala SDK to 2.12.3 to avoid `java.lang.NoSuchMethodError`
- Creation of stand-alone Ant distribution is triggered by Maven profile
- Use a meaningful name for tenants, e.g. `github` and `gatling`

## 2.2.5.1

### Added
- Provide a `DefaultHttpProtocolBuilder` to avoid boiler-plate code

### Removed
- Removing the `wiremock` configuration since it is not used

## 2.2.5.0

### Changed
- Upgraded to Gatling 2.2.5
- Avoid parsing the JSON response but use `bodyString` instead
- Print the currently invoked URL when using the default logging configuration
- Add a `default` and `trace` logging level
- Make `VERSION.txt` and `CHANGELOG.md` available in the Gatling stand-alone distribution
- Add error details to Gatling's `simulation.log`  using an `ExtraInfoExtractor`
- Extracted HttpProtocolBuilder to simplify test setup
- Using `bodyString` instead of `jsonPath` to avoid parsing JSON if not required
- Added a `before` and `after` hook to document the feature 

### Fixed
- Stop the Gatling JVM when running tests from the Windows command line

# RestAPIWithAI

Scalable REST API automation framework for [GoREST](https://gorest.co.in/) using Java 17, Rest Assured, TestNG, Jackson POJOs, JSON-driven test data, JSON Schema validation, Log4j2 and Allure.

## Prerequisites
- Java 17+
- Maven 3.9+
- A GoREST access token

## Authentication
Never commit the token. Set it as an environment variable:

**macOS/Linux**
```bash
export GOREST_TOKEN="your_token"
```

**PowerShell**
```powershell
$env:GOREST_TOKEN="your_token"
```

## Run
```bash
mvn clean test
```

## Allure
Results are written to `target/allure-results`. If Allure CLI is installed:
```bash
allure serve target/allure-results
```

## Architecture
Tests -> UserApiClient -> reusable RequestSpecFactory -> Rest Assured -> GoREST.

Tests own assertions; the API client owns HTTP transport. Test data is grouped by TestNG method name in `src/test/resources/data/user-test-data.json`.

## Coverage
Positive and negative automation for create, get, patch/update and delete user endpoints. Update uses HTTP 200, matching the current GoREST PATCH contract.

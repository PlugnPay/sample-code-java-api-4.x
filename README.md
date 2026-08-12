# sample-code-java-api-4.x
Java API 4.x (Remote Client / API)

**************************************************************************
** This code is NOT officially supported by Plug 'n Pay Technologies Inc. **
**************************************************************************

**Deprecated:** This Java API 4.x (`pnp4.TransactionEngine`) payment method
is deprecated. New integrations should use the normal Remote API
(`pnpremote.cgi`) directly with a current HTTPS client library. Existing
implementations may continue to work, but this approach is no longer
recommended.

Current version: **4.0.20070222**

You will need to understand the Remote Client Specification documentation
in order to use this module. It is located in the Documentation/FAQ section
of the admin area. Generated API docs are under `javaapi4/javadoc/`.

## Requirements

* Java with HTTPS / `javax.net.ssl` support (JDK 5+ era or compatible)
* HTTPS outbound access to `https://pay1.plugnpay.com/payment/pnpremote.cgi`
* A live PlugnPay gateway account
* Your account's **Remote Client Password** (sent as `publisher-password`,
  next to `publisher-name` in the request Properties)

## Installation

1. Add the `javaapi4` directory to your classpath (or compile the `pnp4`
   package into your project).
2. Compile the API classes, for example from `javaapi4/`:

```bash
javac pnp4/TransactionEngine.java pnp4/TransactionEngineException.java
javac testTransaction.java
```

3. Run the sample (optional):

```bash
java testTransaction
```

If you used an older copy of this API, read `javaapi4/Changes` before
upgrading — decode behavior and the `javaapiversion` field were updated in
4.0.20070222.

## Usage

Create a `TransactionEngine`, put Remote Client fields into a
`java.util.Properties` object, then call `doTransaction`. The gateway
response is returned as another `Properties` list. Use `apiVersion()` to
print the built-in API version string. An optional constructor argument
overrides the default endpoint URL (must be HTTPS).

Required / common fields:

* `publisher-name` — your PlugnPay gateway account username
* `publisher-password` — your Remote Client Password (same place/manner as
  `publisher-name`)
* `publisher-email` — contact email for the transaction
* `card-name`, `card-number`, `card-exp`, `card-amount`, and other Remote
  Client fields as needed

Example:

```java
TransactionEngine pnp = new TransactionEngine();
Properties pairs = new Properties();
pairs.put("publisher-name", "pnpdemo");
pairs.put("publisher-password", "your_remote_client_password");
pairs.put("publisher-email", "test@plugnpay.com");
pairs.put("mode", "auth");
pairs.put("card-name", "Test");
pairs.put("card-number", "4111111111111111");
pairs.put("card-exp", "12/30");
pairs.put("card-amount", "1.00");

Properties results = pnp.doTransaction(pairs);
```

The engine URL-encodes the request (and adds `javaapiversion`) before
POSTing, then URL-decodes the response into Properties. See
`javaapi4/testTransaction.java` for a fuller example, including exception
handling via `TransactionEngineException`.

### Troubleshooting

| Message | Cause |
| --- | --- |
| `Bad TransactionURL set` | Custom endpoint URL is malformed |
| `IO Exception thrown ...` | Network / TLS problem reaching the gateway |
| `result decoding problem ...` | Unexpected or empty gateway response body |
| Blank / missing auth fields | Missing `publisher-name` or `publisher-password` |

## Repository layout

```
sample-code-java-api-4.x/
  README.md
  javaapi4/
    Changes                        # changelog for 4.0.20070222
    testTransaction.java           # example auth + response dump
    pnp4/
      TransactionEngine.java       # HTTPS POST helper (package pnp4)
      TransactionEngineException.java
    javadoc/                       # generated API documentation
```

## Support

Provided AS IS. Limited technical support may be available via PlugnPay's
Online Helpdesk after checking this README and the javadocs.
See [PlugnPay docs](https://docs.plugnpay.com/) and the Remote Client
Specification in Merchant Admin for field details.

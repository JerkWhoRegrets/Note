# EMV 3DS v2.2.0 Data Elements - Part 2

## 1. Scope and source rules

This batch captures every complete Table A.1 row from **Authentication Method** (`authenticationMethod`) on physical PDF page 167 through **Cardholder Shipping Address Country** (`shipAddrCountry`) on physical PDF page 185. The scoped range contains 33 rows. Physical page 166 ends the preceding batch with Address Match Indicator, and physical page 186 begins the next batch with Cardholder Shipping Address Line 1. [EMV 3DS v2.2.0, section A.4, pp. 166-186, Table A.1; no local numbered Req]

The `R`, `C`, and `O` inclusion rules, missing-field behavior, edit criteria, channel codes, and message-category codes are defined in Part 1 and remain applicable here. In particular, `C` preserves the row's stated condition; it is not equivalent to always required. [EMV 3DS v2.2.0, Annex A and sections A.1-A.2, pp. 145-147; no local numbered Req]

Each entry below separates the Table A.1 source statement from a bounded issuer ACS implementation observation. A privacy/security observation does not add a protocol requirement. Unless a row expressly says otherwise, the scoped Table A.1 rows do not classify the field, require field-level encryption, prescribe storage, or define logging/redaction policy. [EMV 3DS v2.2.0, section A.4, pp. 167-185, Table A.1; no local numbered Reqs]

## 2. Data-element register

### Physical PDF page 167

#### 2.1 Authentication Method - `authenticationMethod`

- **Purpose and component responsibility:** Authentication approach used by the ACS to authenticate the Cardholder for the specific transaction. Source: ACS. The field appears only in the ACS-to-DS RReq and is not passed in the DS-to-3DS Server RReq. For `03-3RI`, it is present only for Decoupled Authentication.
- **Type/format/values:** String, exactly 2 characters. `01` Static Passcode; `02` SMS OTP; `03` Key fob or EMV card reader OTP; `04` App OTP; `05` OTP Other; `06` KBA; `07` OOB Biometrics; `08` OOB Login; `09` OOB Other; `10` Other; `11` Push Confirmation; `12-79` reserved for future EMVCo use and invalid until defined; `80-99` reserved for DS use.
- **Applicability/presence:** `01-APP`, `02-BRW`, and `03-3RI`; `01-PA` and `02-NPA`; `RReq = C`. Conditional text says it is required to be sent by the ACS, subject to the route and 3RI qualifications above.
- **Privacy/security:** Source statement: it identifies the authentication approach used for the transaction. Issuer ACS observation (non-normative): treat it as security-relevant result metadata and avoid exposing it beyond authorised result processing.
- **Citation/Req:** [EMV 3DS v2.2.0, section A.4, p. 167, Table A.1; no local numbered Req]

### Physical PDF page 168

#### 2.2 Authentication Type - `authenticationType`

- **Purpose and component responsibility:** Identifies the authentication-method type the Issuer will use to challenge the Cardholder in ARes, or the type the ACS used as reported in RReq. Source: ACS.
- **Type/format/values:** String, exactly 2 characters. `01` Static; `02` Dynamic; `03` OOB; `04` Decoupled; `05-79` reserved for future EMVCo use and invalid until defined; `80-99` reserved for DS use.
- **Applicability/presence:** `01-APP`, `02-BRW`, and `03-3RI`; `01-PA` and `02-NPA`; `ARes = C`, required when ARes Transaction Status is `C` or `D`; `RReq = C`, required when RReq Transaction Status is `Y` or `N`.
- **Privacy/security:** Source statement: it communicates the planned or used authentication class. Issuer ACS observation (non-normative): keep ARes selection and RReq reporting consistent with the actual issuer authentication path.
- **Citation/Req:** [EMV 3DS v2.2.0, section A.4, p. 168, Table A.1; no local numbered Req]

### Physical PDF page 169

#### 2.3 Authentication Value - `authenticationValue`

- **Purpose and component responsibility:** Payment System-specific value provided by the ACS or DS using an algorithm defined by the Payment System; it may provide proof of authentication. Sources: ACS and DS.
- **Type/format/values:** String, exactly 28 characters. Accepted value is a 20-byte value Base64 encoded, described by the row as giving a 28-byte result.
- **Applicability/presence:** `01-APP`, `02-BRW`, and `03-3RI`; `01-PA` and `02-NPA`; `ARes = C`, `RReq = C`. For `01-PA`, required when Transaction Status is `Y` or `A`; conditional on DS rules when status is `I`; omitted from RReq when sent as an abandonment notification. For `02-NPA`, conditional on DS rules.
- **Privacy/security:** Source statement: the value may prove authentication and its algorithm is Payment System-specific. Issuer ACS observation (non-normative): handle as sensitive authentication-result material and obtain the applicable scheme/DS generation, verification, retention, and redaction rules.
- **Citation/Req:** [EMV 3DS v2.2.0, section A.4, p. 169, Table A.1; no local numbered Req]

#### 2.4 Broadcast Information - `broadInfo`

- **Purpose and component responsibility:** Unstructured information exchanged among the 3DS Server, DS, and ACS. Sources: 3DS Server, DS, and ACS.
- **Type/format/values:** JSON Object, variable length, maximum 4096 characters; no value table is referenced.
- **Applicability/presence:** `01-APP`, `02-BRW`, and `03-3RI`; `01-PA` and `02-NPA`; `AReq = C`, `ARes = C`; presence requirements are DS-specific.
- **Privacy/security:** Source statement: the content is unstructured and DS-specific. Issuer ACS observation (non-normative): define schema allow-listing, size enforcement, safe logging, and data-minimisation rules under the applicable DS profile; do not infer semantics from this Core row.
- **Citation/Req:** [EMV 3DS v2.2.0, section A.4, p. 169, Table A.1; no local numbered Req]

### Physical PDF page 170

#### 2.5 Browser Accept Headers - `browserAcceptHeader`

- **Purpose and component responsibility:** Exact HTTP Accept header content sent by the Cardholder browser to the 3DS Requestor. Source: 3DS Server.
- **Type/format/values:** String, variable length, maximum 2048 characters. If the browser value exceeds 2048 characters, the 3DS Server truncates the excess. The row refers to section A.5.2 for additional detail.
- **Applicability/presence:** `02-BRW`; `01-PA` and `02-NPA`; `AReq = R`; no additional conditional-inclusion text.
- **Privacy/security:** Source statement: this is transaction-specific browser information used by the ACS to determine browser authentication support; section A.5.2 assigns the 3DS Server responsibility for accurate, unaltered, non-hard-coded data unique to each transaction. Issuer ACS observation (non-normative): browser characteristics can contribute to device/browser fingerprinting and should be access-controlled and redacted from general logs.
- **Citation/Req:** [EMV 3DS v2.2.0, section A.4, p. 170, Table A.1; section A.5.2, p. 223; no local numbered Req]

### Physical PDF page 171

#### 2.6 Browser IP Address - `browserIP`

- **Purpose and component responsibility:** Browser IP address returned by HTTP headers to the 3DS Requestor. Source: 3DS Server.
- **Type/format/values:** String, variable length, maximum 45 characters. IPv4 is represented as four dotted-decimal sets, each `0-255` (example `1.12.123.255`). IPv6 is represented as eight colon-separated groups of four hexadecimal digits, each group representing 16 bits (example printed as `2011:0db8:85a3:0101:0101:8a2e:0370:7334`). The row refers to section A.5.2.
- **Applicability/presence:** `02-BRW`; `01-PA` and `02-NPA`; `AReq = C`; the field **shall** be included where regionally acceptable.
- **Privacy/security:** Source statement: browser information must satisfy the section A.5.2 accuracy, non-alteration, non-hard-coding, and per-transaction rules. Issuer ACS observation (non-normative): an IP address is privacy- and security-relevant network data; define lawful use, access, retention, masking, and proxy-handling policy outside this row.
- **Citation/Req:** [EMV 3DS v2.2.0, section A.4, p. 171, Table A.1; section A.5.2, p. 223; no local numbered Req]

### Physical PDF page 172

#### 2.7 Browser Java Enabled - `browserJavaEnabled`

- **Purpose and component responsibility:** Boolean representing whether the Cardholder browser can execute Java, returned from `navigator.javaEnabled`. Source: 3DS Server; the row refers to section A.5.2.
- **Type/format/values:** JSON Boolean; accepted values `true` and `false`.
- **Applicability/presence:** `02-BRW`; `01-PA` and `02-NPA`; `AReq = C`; required when Browser JavaScript Enabled is `true`, otherwise optional.
- **Privacy/security:** Source statement: browser information is to be accurate, unaltered, non-hard-coded, and unique to the transaction under section A.5.2. Issuer ACS observation (non-normative): use as capability/risk context, not as proof of Cardholder identity.
- **Citation/Req:** [EMV 3DS v2.2.0, section A.4, p. 172, Table A.1; section A.5.2, p. 223; no local numbered Req]

#### 2.8 Browser JavaScript Enabled - `browserJavascriptEnabled`

- **Purpose and component responsibility:** Boolean representing whether the Cardholder browser can execute JavaScript. Source: 3DS Server; the row refers to section A.5.2.
- **Type/format/values:** JSON Boolean; accepted values `true` and `false`.
- **Applicability/presence:** `02-BRW`; `01-PA` and `02-NPA`; `AReq = R`; no additional conditional-inclusion text.
- **Privacy/security:** Source statement: this is required browser capability data. Issuer ACS observation (non-normative): capability flags influence collection and challenge rendering but are not authenticators. The cross-reference mismatch with section A.5.2 is recorded as unresolved.
- **Citation/Req:** [EMV 3DS v2.2.0, section A.4, p. 172, Table A.1; section A.5.2, p. 223; no local numbered Req]

#### 2.9 Browser Language - `browserLanguage`

- **Purpose and component responsibility:** Browser language as defined in IETF BCP 47, returned from `navigator.language`. Source: 3DS Server; the row refers to section A.5.2.
- **Type/format/values:** String, variable length, 1-8 characters; value is an IETF BCP 47 language tag.
- **Applicability/presence:** `02-BRW`; `01-PA` and `02-NPA`; `AReq = R`; no additional conditional-inclusion text.
- **Privacy/security:** Source statement: transaction-specific browser information used for ACS browser support. Issuer ACS observation (non-normative): use for supported localisation decisions while applying fallback behavior outside this row; language can also be fingerprinting data.
- **Citation/Req:** [EMV 3DS v2.2.0, section A.4, p. 172, Table A.1; section A.5.2, p. 223; no local numbered Req]

### Physical PDF page 173

#### 2.10 Browser Screen Color Depth - `browserColorDepth`

- **Purpose and component responsibility:** Bit depth of the browser colour palette, in bits per pixel, obtained from `screen.colorDepth`. Source: 3DS Server; the row refers to section A.5.2.
- **Type/format/values:** String, 1-2 characters. Accepted values: `1`, `4`, `8`, `15`, `16`, `24`, `32`, and `48`, representing the same number of bits.
- **Applicability/presence:** `02-BRW`; `01-PA` and `02-NPA`; `AReq = C`; required when Browser JavaScript Enabled is `true`, otherwise optional.
- **Privacy/security:** Source statement: transaction-specific browser information. Issuer ACS observation (non-normative): screen characteristics can be fingerprinting signals; do not treat them as identity proof.
- **Citation/Req:** [EMV 3DS v2.2.0, section A.4, p. 173, Table A.1; section A.5.2, p. 223; no local numbered Req]

#### 2.11 Browser Screen Height - `browserScreenHeight`

- **Purpose and component responsibility:** Total Cardholder screen height in pixels, returned from `screen.height`. Source: 3DS Server; the row refers to section A.5.2.
- **Type/format/values:** Numeric String, variable length, 1-6 characters.
- **Applicability/presence:** `02-BRW`; `01-PA` and `02-NPA`; `AReq = C`; required when Browser JavaScript Enabled is `true`, otherwise optional.
- **Privacy/security:** Source statement: transaction-specific browser information. Issuer ACS observation (non-normative): use for rendering/risk context and handle as potential fingerprinting data.
- **Citation/Req:** [EMV 3DS v2.2.0, section A.4, p. 173, Table A.1; section A.5.2, p. 223; no local numbered Req]

#### 2.12 Browser Screen Width - `browserScreenWidth`

- **Purpose and component responsibility:** Total Cardholder screen width in pixels, returned from `screen.width`. Source: 3DS Server; the row refers to section A.5.2.
- **Type/format/values:** Numeric String, variable length, 1-6 characters.
- **Applicability/presence:** `02-BRW`; `01-PA` and `02-NPA`; `AReq = C`; required when Browser JavaScript Enabled is `true`, otherwise optional.
- **Privacy/security:** Source statement: transaction-specific browser information. Issuer ACS observation (non-normative): use for rendering/risk context and handle as potential fingerprinting data.
- **Citation/Req:** [EMV 3DS v2.2.0, section A.4, p. 173, Table A.1; section A.5.2, p. 223; no local numbered Req]

### Physical PDF page 174

#### 2.13 Browser Time Zone - `browserTZ`

- **Purpose and component responsibility:** Offset in minutes between UTC and the Cardholder browser local time. The offset is positive when local time is behind UTC and negative when it is ahead. Source: 3DS Server.
- **Type/format/values:** String, variable length, 1-5 characters; returned by `getTimezoneOffset()`. Examples: UTC -5 hours is `300` or `+300`; UTC +5 hours is `-300`. The row refers to section A.5.2.
- **Applicability/presence:** `02-BRW`; `01-PA` and `02-NPA`; `AReq = C`; required when Browser JavaScript Enabled is `true`, otherwise optional.
- **Privacy/security:** Source statement: transaction-specific browser information. Issuer ACS observation (non-normative): preserve the source sign convention exactly and handle the value as locale/fingerprinting context, not direct location proof.
- **Citation/Req:** [EMV 3DS v2.2.0, section A.4, p. 174, Table A.1; section A.5.2, p. 223; no local numbered Req]

### Physical PDF page 175

#### 2.14 Browser User-Agent - `browserUserAgent`

- **Purpose and component responsibility:** Exact HTTP User-Agent header content. Source: 3DS Server.
- **Type/format/values:** String, variable length, maximum 2048 characters. If the browser value exceeds 2048 characters, the 3DS Server truncates the excess. The row refers to section A.5.2.
- **Applicability/presence:** `02-BRW`; `01-PA` and `02-NPA`; `AReq = R`; no additional conditional-inclusion text.
- **Privacy/security:** Source statement: transaction-specific browser information subject to section A.5.2 accuracy and non-hard-coding responsibility. Issuer ACS observation (non-normative): User-Agent data is useful for compatibility and risk context and can contribute to fingerprinting; apply data-minimisation and safe-logging controls.
- **Citation/Req:** [EMV 3DS v2.2.0, section A.4, p. 175, Table A.1; section A.5.2, p. 223; no local numbered Req]

#### 2.15 Card Range Data - `cardRangeData`

- **Purpose and component responsibility:** DS-provided card-range data identifying the latest protocol versions supported by the ACS and, optionally, the DS hosting the range and configured 3DS Method ACS URL; it also identifies supported ACS features such as Whitelisting or Decoupled Authentication. There may be as many JSON objects as stored card ranges in the DS. Source: DS.
- **Type/format/values:** JSON Array, variable length. Refer to Table A.6 for Card Range Data elements.
- **Applicability/presence:** Device Channel `N/A`; Message Category `N/A`; `PRes = O`; no additional conditional-inclusion text.
- **Privacy/security:** Source statement: capability, version, range, and optional endpoint data supplied by the DS. Issuer ACS observation (non-normative): this is DS/3DS Server preparation data rather than an ACS AReq input; protect configuration integrity and do not infer Table A.6 contents in this batch.
- **Citation/Req:** [EMV 3DS v2.2.0, section A.4, p. 175, Table A.1; no local numbered Req]

### Physical PDF page 176

#### 2.16 Card/Token Expiry Date - `cardExpiryDate`

- **Purpose and component responsibility:** Expiry date of the PAN or token supplied to the 3DS Requestor by the Cardholder. Source: 3DS Server.
- **Type/format/values:** String, exactly 4 characters; format `YYMM`.
- **Applicability/presence:** `01-APP`, `02-BRW`, and `03-3RI`; `01-PA` and `02-NPA`; `AReq = C`; presence requirements are DS-specific.
- **Privacy/security:** Source statement: credential expiry metadata. Issuer ACS observation (non-normative): handle with the account number/token under applicable payment-data, privacy, and logging controls; the row does not authorise storage or display.
- **Citation/Req:** [EMV 3DS v2.2.0, section A.4, p. 176, Table A.1; no local numbered Req]

#### 2.17 Cardholder Account Information - `acctInfo`

- **Purpose and component responsibility:** Additional Cardholder account information provided by the 3DS Requestor. Source: 3DS Server.
- **Type/format/values:** JSON Object, variable length. Refer to Table A.8 for Cardholder Account Information data elements.
- **Applicability/presence:** `01-APP`, `02-BRW`, and `03-3RI`; `01-PA` and `02-NPA`; `AReq = O`; optional, but strongly recommended to include.
- **Privacy/security:** Source statement: additional Cardholder account information. Issuer ACS observation (non-normative): likely risk-decision input and privacy-relevant account history/context; apply purpose limitation, access control, retention, and redaction after resolving Table A.8 in its later batch.
- **Citation/Req:** [EMV 3DS v2.2.0, section A.4, p. 176, Table A.1; no local numbered Req]

#### 2.18 Cardholder Account Number - `acctNumber`

- **Purpose and component responsibility:** Account number used in the authorisation request for payment transactions; may be represented by a PAN or token. Source: 3DS Server.
- **Type/format/values:** String, variable length, 13-19 characters; format represented by ISO 7812.
- **Applicability/presence:** `01-APP`, `02-BRW`, and `03-3RI`; `01-PA` and `02-NPA`; `AReq = R`; no additional conditional-inclusion text.
- **Privacy/security:** Source statement: PAN or token used for the transaction. Issuer ACS observation (non-normative): treat as highly sensitive payment/account data, minimise exposure, mask/redact logs and displays, and apply all applicable scheme, PCI, privacy, token, and issuer controls; the row itself states no field-level encryption.
- **Citation/Req:** [EMV 3DS v2.2.0, section A.4, p. 176, Table A.1; no local numbered Req]

#### 2.19 Cardholder Account Identifier - `acctID`

- **Purpose and component responsibility:** Additional account information optionally provided by the 3DS Requestor. Source: 3DS Server.
- **Type/format/values:** String, variable length, maximum 64 characters; no value table is referenced.
- **Applicability/presence:** `01-APP`, `02-BRW`, and `03-3RI`; `01-PA` and `02-NPA`; `AReq = O`; no additional conditional-inclusion text.
- **Privacy/security:** Source statement: additional account identifier/information. Issuer ACS observation (non-normative): treat as potentially identifying and linkable data; define accepted semantics and avoid assuming it is a PAN, token, or issuer account key.
- **Citation/Req:** [EMV 3DS v2.2.0, section A.4, p. 176, Table A.1; no local numbered Req]

### Physical PDF page 177

#### 2.20 Cardholder Billing Address City - `billAddrCity`

- **Purpose and component responsibility:** City of the Cardholder billing address associated with the card used for the purchase. Source: 3DS Server.
- **Type/format/values:** String, variable length, maximum 50 characters.
- **Applicability/presence:** `01-APP`, `02-BRW`, and `03-3RI`; `01-PA` and `02-NPA`; `AReq = C`. For `01-PA`, required unless a market or regional mandate restricts sending it. For `02-NPA`, required if available unless such a mandate restricts sending it.
- **Privacy/security:** Source statement: Cardholder billing-location data. Issuer ACS observation (non-normative): use only for authorised authentication/risk purposes and apply privacy, access, retention, masking, and safe-logging controls.
- **Citation/Req:** [EMV 3DS v2.2.0, section A.4, p. 177, Table A.1; no local numbered Req]

### Physical PDF page 178

#### 2.21 Cardholder Billing Address Country - `billAddrCountry`

- **Purpose and component responsibility:** Country of the Cardholder billing address associated with the card used for the purchase. Source: 3DS Server.
- **Type/format/values:** String, exactly 3 characters; **shall** be the ISO 3166-1 numeric three-digit country code other than the exceptions listed in Table A.5.
- **Applicability/presence:** `01-APP`, `02-BRW`, and `03-3RI`; `01-PA` and `02-NPA`; `AReq = C`. Required if Cardholder Billing Address State is present. For `01-PA`, also required unless a market or regional mandate restricts sending it. For `02-NPA`, required if available unless such a mandate restricts sending it.
- **Privacy/security:** Source statement: billing-country data. Issuer ACS observation (non-normative): validate the printed ISO/Table A.5 rule and handle as privacy-relevant location/account data.
- **Citation/Req:** [EMV 3DS v2.2.0, section A.4, p. 178, Table A.1; no local numbered Req]

### Physical PDF page 179

#### 2.22 Cardholder Billing Address Line 1 - `billAddrLine1`

- **Purpose and component responsibility:** First street-address line or equivalent local portion of the Cardholder billing address associated with the card used for the purchase. Source: 3DS Server.
- **Type/format/values:** String, variable length, maximum 50 characters.
- **Applicability/presence:** `01-APP`, `02-BRW`, and `03-3RI`; `01-PA` and `02-NPA`; `AReq = C`. For `01-PA`, required unless a market or regional mandate restricts sending it. For `02-NPA`, required if available unless such a mandate restricts sending it.
- **Privacy/security:** Source statement: detailed billing address. Issuer ACS observation (non-normative): this is directly identifying location data; minimise use and tightly control logs, support views, retention, and exports.
- **Citation/Req:** [EMV 3DS v2.2.0, section A.4, p. 179, Table A.1; no local numbered Req]

#### 2.23 Cardholder Billing Address Line 2 - `billAddrLine2`

- **Purpose and component responsibility:** Second street-address line or equivalent local portion of the Cardholder billing address associated with the card used for the purchase. Source: 3DS Server.
- **Type/format/values:** String, variable length, maximum 50 characters.
- **Applicability/presence:** `01-APP`, `02-BRW`, and `03-3RI`; `01-PA` and `02-NPA`; `AReq = C`; required if available unless a market or regional mandate restricts sending it.
- **Privacy/security:** Source statement: detailed billing address. Issuer ACS observation (non-normative): this is directly identifying location data; minimise use and tightly control logs, support views, retention, and exports.
- **Citation/Req:** [EMV 3DS v2.2.0, section A.4, p. 179, Table A.1; no local numbered Req]

#### 2.24 Cardholder Billing Address Line 3 - `billAddrLine3`

- **Purpose and component responsibility:** Third street-address line or equivalent local portion of the Cardholder billing address associated with the card used for the purchase. Source: 3DS Server.
- **Type/format/values:** String, variable length, maximum 50 characters.
- **Applicability/presence:** `01-APP`, `02-BRW`, and `03-3RI`; `01-PA` and `02-NPA`; `AReq = C`; required if available unless a market or regional mandate restricts sending it.
- **Privacy/security:** Source statement: detailed billing address. Issuer ACS observation (non-normative): this is directly identifying location data; minimise use and tightly control logs, support views, retention, and exports.
- **Citation/Req:** [EMV 3DS v2.2.0, section A.4, p. 179, Table A.1; no local numbered Req]

### Physical PDF page 180

#### 2.25 Cardholder Billing Address Postal Code - `billAddrPostCode`

- **Purpose and component responsibility:** ZIP or other postal code of the Cardholder billing address associated with the card used for the purchase. Source: 3DS Server.
- **Type/format/values:** String, variable length, maximum 16 characters.
- **Applicability/presence:** `01-APP`, `02-BRW`, and `03-3RI`; `01-PA` and `02-NPA`; `AReq = C`. For `01-PA`, required unless a market or regional mandate restricts sending it. For `02-NPA`, required if available unless such a mandate restricts sending it.
- **Privacy/security:** Source statement: billing-location data. Issuer ACS observation (non-normative): treat as privacy-relevant location/account information and avoid unnecessary exposure in logs or support tooling.
- **Citation/Req:** [EMV 3DS v2.2.0, section A.4, p. 180, Table A.1; no local numbered Req]

### Physical PDF page 181

#### 2.26 Cardholder Billing Address State - `billAddrState`

- **Purpose and component responsibility:** State or province of the Cardholder billing address associated with the card used for the purchase. Source: 3DS Server.
- **Type/format/values:** String, variable length, maximum 3 characters; **should** be the country subdivision code defined in ISO 3166-2.
- **Applicability/presence:** `01-APP`, `02-BRW`, and `03-3RI`; `01-PA` and `02-NPA`; `AReq = C`. For `01-PA`, required unless a market or regional mandate restricts sending it or State is not applicable for the country. For `02-NPA`, required if available unless a mandate restricts sending it or State is not applicable for the country.
- **Privacy/security:** Source statement: billing-location data and a `should`, not `shall`, format rule. Issuer ACS observation (non-normative): preserve the conditional country applicability and avoid upgrading the ISO 3166-2 recommendation into a rejection rule not stated by the source.
- **Citation/Req:** [EMV 3DS v2.2.0, section A.4, p. 181, Table A.1; no local numbered Req]

#### 2.27 Cardholder Email Address - `email`

- **Purpose and component responsibility:** Email address associated with the account, either entered by the Cardholder or held by the 3DS Requestor. Source: 3DS Server.
- **Type/format/values:** String, variable length, maximum 254 characters; **shall** meet section 3.4 of IETF RFC 5322.
- **Applicability/presence:** `01-APP`, `02-BRW`, and `03-3RI`; `01-PA` and `02-NPA`; `AReq = C`; required unless a market or regional mandate restricts sending it.
- **Privacy/security:** Source statement: account-linked contact data. Issuer ACS observation (non-normative): treat as directly identifying personal data; restrict access, prevent unintended messaging, and redact general logs and analytics.
- **Citation/Req:** [EMV 3DS v2.2.0, section A.4, p. 181, Table A.1; no local numbered Req]

### Physical PDF page 182

#### 2.28 Cardholder Home Phone Number - `homePhone`

- **Purpose and component responsibility:** Home phone number provided by the Cardholder. Source: 3DS Server.
- **Type/format/values:** Variable JSON Object containing String fields `cc` (1-3 characters) and `subscriber` (variable, maximum 15 characters). Country Code and Subscriber portions follow the named fields; refer to ITU E.164 for additional format and length information. The row includes an example with `cc: "1"` and `subscriber: "1234567899"`.
- **Applicability/presence:** `01-APP`, `02-BRW`, and `03-3RI`; `01-PA` and `02-NPA`; `AReq = C`; required if available unless a market or regional mandate restricts sending it.
- **Privacy/security:** Source statement: Cardholder contact data. Issuer ACS observation (non-normative): treat as directly identifying personal data; restrict access and redact or mask logs, analytics, and support displays.
- **Citation/Req:** [EMV 3DS v2.2.0, section A.4, p. 182, Table A.1; no local numbered Req]

### Physical PDF page 183

#### 2.29 Cardholder Information Text - `cardholderInfo`

- **Purpose and component responsibility:** Text supplied by the ACS/Issuer to the Cardholder during a Frictionless or Decoupled transaction. The example tells the Cardholder that additional authentication is needed and to contact the Issuer. Source: ACS.
- **Type/format/values:** String, variable length, maximum 128 characters. If populated, the merchant is required to convey the information to the Cardholder.
- **Applicability/presence:** `01-APP`, `02-BRW`, and `03-3RI`; `01-PA` and `02-NPA`; `ARes = C`; required when ACS Decoupled Confirmation Indicator is `Y`, otherwise optional for the ACS.
- **Privacy/security:** Source statement: issuer-to-Cardholder text may include issuer contact/instruction content. Issuer ACS observation (non-normative): use controlled, localised templates that avoid secrets, full account numbers, unsafe links, or transaction data not needed by the Cardholder; delivery behavior outside the row remains subject to the previously logged App/3RI wording questions.
- **Citation/Req:** [EMV 3DS v2.2.0, section A.4, p. 183, Table A.1; no local numbered Req]

### Physical PDF page 184

#### 2.30 Cardholder Mobile Phone Number - `mobilePhone`

- **Purpose and component responsibility:** Mobile phone number provided by the Cardholder. Source: 3DS Server.
- **Type/format/values:** Variable JSON Object containing String fields `cc` (1-3 characters) and `subscriber` (variable, maximum 15 characters). Country Code and Subscriber portions follow the named fields; refer to ITU E.164 for additional format and length information. The row includes an example with `cc: "1"` and `subscriber: "1234567899"`.
- **Applicability/presence:** `01-APP`, `02-BRW`, and `03-3RI`; `01-PA` and `02-NPA`; `AReq = C`; required if available unless a market or regional mandate restricts sending it.
- **Privacy/security:** Source statement: Cardholder contact data. Issuer ACS observation (non-normative): treat as directly identifying personal data; restrict access and redact or mask logs, analytics, and support displays. The row does not say that the number is verified or is the destination used for authentication.
- **Citation/Req:** [EMV 3DS v2.2.0, section A.4, p. 184, Table A.1; no local numbered Req]

### Physical PDF page 185

#### 2.31 Cardholder Name - `cardholderName`

- **Purpose and component responsibility:** Name of the Cardholder. Source: 3DS Server.
- **Type/format/values:** String, variable length, 2-45 characters; alphanumeric special characters listed in EMV Book 4, Appendix B.
- **Applicability/presence:** `01-APP`, `02-BRW`, and `03-3RI`; `01-PA` and `02-NPA`; `AReq = C`; required unless a market or regional mandate restricts sending it.
- **Privacy/security:** Source statement: Cardholder identity data. Issuer ACS observation (non-normative): treat as directly identifying personal data, avoid broad display/logging, and do not infer name matching or identity-verification rules from this row.
- **Citation/Req:** [EMV 3DS v2.2.0, section A.4, p. 185, Table A.1; no local numbered Req]

#### 2.32 Cardholder Shipping Address City - `shipAddrCity`

- **Purpose and component responsibility:** City portion of the shipping address requested by the Cardholder. Source: 3DS Server.
- **Type/format/values:** String, variable length, maximum 50 characters.
- **Applicability/presence:** `01-APP`, `02-BRW`, and `03-3RI`; `01-PA` and `02-NPA`; `AReq = C`; required if available unless a market or regional mandate restricts sending it.
- **Privacy/security:** Source statement: requested shipping-location data. Issuer ACS observation (non-normative): treat as privacy-relevant location data and tightly control logs, analytics, support access, retention, and exports.
- **Citation/Req:** [EMV 3DS v2.2.0, section A.4, p. 185, Table A.1; no local numbered Req]

#### 2.33 Cardholder Shipping Address Country - `shipAddrCountry`

- **Purpose and component responsibility:** Country of the shipping address requested by the Cardholder. Source: 3DS Server.
- **Type/format/values:** String, exactly 3 characters; ISO 3166-1 three-digit country code other than those listed in Table A.5.
- **Applicability/presence:** `01-APP`, `02-BRW`, and `03-3RI`; `01-PA` and `02-NPA`; `AReq = C`; required if Cardholder Shipping Address State is present; also required if available unless a market or regional mandate restricts sending it.
- **Privacy/security:** Source statement: requested shipping-country data. Issuer ACS observation (non-normative): validate the printed ISO/Table A.5 rule and handle as privacy-relevant location data.
- **Citation/Req:** [EMV 3DS v2.2.0, section A.4, p. 185, Table A.1; no local numbered Req]

## 3. ACS behavior traceability

| ACS behavior area | Material Part 2 elements | Source-prescribed effect | Evidence |
|---|---|---|---|
| Authentication approach and result reporting | `authenticationMethod`, `authenticationType`, `authenticationValue` | ACS reports the method actually used in its outbound RReq, reports the planned/used authentication type under the status conditions, and supplies the Payment System-specific Authentication Value under the category/status/DS rules. | [EMV 3DS v2.2.0, section A.4, pp. 167-169, Table A.1; no local numbered Reqs] |
| Browser capability and environment input | `browserAcceptHeader`, `browserIP`, `browserJavaEnabled`, `browserJavascriptEnabled`, `browserLanguage`, `browserColorDepth`, `browserScreenHeight`, `browserScreenWidth`, `browserTZ`, `browserUserAgent` | The 3DS Server supplies the required and conditional Browser AReq fields. Section A.5.2 assigns it responsibility for accurate, unaltered, non-hard-coded, per-transaction browser information used by the ACS to determine authentication support. | [EMV 3DS v2.2.0, section A.4, pp. 170-175, Table A.1; section A.5.2, p. 223; no local numbered Reqs] |
| Account and credential input | `cardExpiryDate`, `acctInfo`, `acctNumber`, `acctID` | ACS receives required account number, DS-conditional expiry, optional-but-strongly-recommended Account Information, and optional Account Identifier under their printed formats. | [EMV 3DS v2.2.0, section A.4, p. 176, Table A.1; no local numbered Reqs] |
| Billing information | `billAddrCity`, `billAddrCountry`, `billAddrLine1`, `billAddrLine2`, `billAddrLine3`, `billAddrPostCode`, `billAddrState` | Fields are conditional AReq inputs. Payment and non-payment availability rules differ for several fields; market/regional restrictions and state applicability must be preserved. Country uses ISO 3166-1/Table A.5; state **should** use ISO 3166-2. | [EMV 3DS v2.2.0, section A.4, pp. 177-181, Table A.1; no local numbered Reqs] |
| Cardholder contact and identity | `email`, `homePhone`, `mobilePhone`, `cardholderName` | Conditional AReq inputs carry RFC 5322 email, ITU E.164-structured phone objects, and EMV Book 4 character-constrained name data, subject to availability/mandate conditions. | [EMV 3DS v2.2.0, section A.4, pp. 181-185, Table A.1; no local numbered Reqs] |
| Issuer-to-Cardholder Frictionless/Decoupled text | `cardholderInfo` | ACS must include the ARes field when ACS Decoupled Confirmation Indicator is `Y`; otherwise it is optional for ACS. If populated, the merchant is required to convey it to the Cardholder. | [EMV 3DS v2.2.0, section A.4, p. 183, Table A.1; no local numbered Req] |
| Shipping information at the batch boundary | `shipAddrCity`, `shipAddrCountry` | City is required if available unless restricted; country is additionally required when shipping state is present and is constrained by ISO 3166-1/Table A.5. Remaining shipping address rows begin on page 186 and are outside this batch. | [EMV 3DS v2.2.0, section A.4, pp. 185-186, Table A.1; no local numbered Reqs] |

## 4. Issuer ACS implementation observations

The following observations are non-normative and are not additional EMV 3DS requirements:

- Treat Authentication Value as scheme-defined authentication proof material; do not design its algorithm, verification, or lifecycle from the Core row alone. [Protocol anchor: EMV 3DS v2.2.0, section A.4, p. 169, Table A.1; no local numbered Req]
- Enforce the exact Table A.1 presence rules before risk evaluation. A conditional address/contact field may be legitimately absent because of category, availability, country applicability, or market/regional restrictions. [Protocol anchor: EMV 3DS v2.2.0, section A.4, pp. 177-185, Table A.1; no local numbered Reqs]
- Separate account/PAN/token, address, name, email, phone, IP, and browser fingerprinting data in access control, telemetry, support tooling, retention, and deletion design. The scoped rows do not define those internal controls. [Protocol anchors: EMV 3DS v2.2.0, section A.4, pp. 170-185, Table A.1; no local numbered Reqs]
- Do not infer that `mobilePhone` is verified, belongs to the account owner, or must be used for SMS OTP; the row states only that it is provided by the Cardholder. [Protocol anchor: EMV 3DS v2.2.0, section A.4, p. 184, Table A.1; no local numbered Req]

## 5. Unresolved items in this batch

- **Section A.5.2 omits Browser JavaScript Enabled from its field list.** The Table A.1 row for `browserJavascriptEnabled` is required in Browser AReq and refers to section A.5.2, but section A.5.2's enumerated transaction-specific browser fields does not list Browser JavaScript Enabled. Preserve the Table A.1 requirement and obtain authoritative clarification before deciding whether the A.5.2 non-alteration/non-hard-coding/uniqueness sentence formally applies to this omitted field. [EMV 3DS v2.2.0, section A.4, p. 172, Table A.1; section A.5.2, p. 223]
- **Authentication Value uses inconsistent units.** The row declares a JSON String of 28 characters but describes Base64 encoding a 20-byte value as giving a 28-byte result. Preserve the 28-character field edit and obtain scheme/DS clarification on the exact Base64 representation and padding rather than treating the printed `28-byte` wording as a separate binary-length requirement. [EMV 3DS v2.2.0, section A.4, p. 169, Table A.1]
- **Section A.5.2 says Browser Information is unique to each transaction.** Several listed browser values can naturally repeat across transactions. Obtain authoritative clarification on whether "unique" means freshly obtained and transaction-associated rather than forcing the values themselves to differ; do not alter or fabricate browser data to create uniqueness. [EMV 3DS v2.2.0, section A.5.2, p. 223]

## 6. Verification record

- Physical PDF pages 167-185 were rendered and directly inspected at readable resolution; physical pages 166 and 186 were also inspected to verify the preceding and following row boundaries.
- The row sequence was compared with `extracted/EMVCo_3DS_Spec_v220_122018-layout.txt`. Count: 33 complete rows.
- First scoped row: physical page 167 begins **Authentication Method** / `authenticationMethod`.
- Last scoped row: physical page 185 ends with **Cardholder Shipping Address Country** / `shipAddrCountry`.
- Page 186 begins **Cardholder Shipping Address Line 1** / `shipAddrLine1`, confirming that the country row is complete and the next row is outside scope.
- No Table A.1 row in the scoped range continues across a physical-page boundary; all row boundaries and multi-line cells were checked visually.
- Section A.5.2 on physical page 223 was directly inspected because the Browser rows reference it; the omitted `browserJavascriptEnabled` list entry is recorded above rather than silently corrected.
- No local numbered requirements appear in the scoped Table A.1 rows or section A.5.2. Each source claim therefore cites the section, physical PDF page, and Table A.1 where applicable without inventing a Req number.

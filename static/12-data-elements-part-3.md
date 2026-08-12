# EMV 3DS v2.2.0 Data Elements - Part 3

## 1. Scope and source rules

This batch captures every complete Table A.1 row from **Cardholder Shipping Address Line 1** (`shipAddrLine1`) on physical PDF page 186 through **Message Extension** (`messageExtension`) on physical PDF page 204. The scoped range contains 44 rows. Physical page 185 ends the preceding batch with Cardholder Shipping Address Country, and physical page 205 begins the next batch with Message Type. [EMV 3DS v2.2.0, section A.4, pp. 185-205, Table A.1; no local numbered Req]

The `R`, `C`, and `O` inclusion rules, missing-field behavior, edit criteria, channel codes, and message-category codes defined in Part 1 remain applicable. A `C` cell preserves its printed condition and is not equivalent to always required. [EMV 3DS v2.2.0, Annex A and sections A.1-A.2, pp. 145-147; no local numbered Req]

Each entry records the canonical data-element name, JSON field name, Table A.1 source component, type/format, applicability, message inclusion, conditionality, and a bounded privacy/security assessment. Where the Source cell is blank, that absence is recorded rather than assigning a component. Unless expressly stated, a privacy/security observation is a non-normative issuer ACS implementation consideration and not an additional protocol requirement. [EMV 3DS v2.2.0, section A.4, pp. 186-204, Table A.1; no local numbered Reqs]

## 2. Data-element register

### Physical PDF page 186

#### 2.1 Cardholder Shipping Address Line 1 - `shipAddrLine1`

- **Purpose and component responsibility:** First street-address line or equivalent local portion of the shipping address requested by the Cardholder. Source: 3DS Server.
- **Type/format/values:** String, variable length, maximum 50 characters.
- **Applicability/presence:** `01-APP`, `02-BRW`, and `03-3RI`; `01-PA` and `02-NPA`; `AReq = C`; required if available unless a market or regional mandate restricts sending it.
- **Privacy/security:** Detailed Cardholder-requested delivery-location data. Issuer ACS observation (non-normative): treat it as directly identifying personal data and avoid unnecessary exposure in logs, analytics, support tools, retention, and exports.
- **Citation/Req:** [EMV 3DS v2.2.0, section A.4, p. 186, Table A.1; no local numbered Req]

#### 2.2 Cardholder Shipping Address Line 2 - `shipAddrLine2`

- **Purpose and component responsibility:** Second street-address line or equivalent local portion of the shipping address requested by the Cardholder. Source: 3DS Server.
- **Type/format/values:** String, variable length, maximum 50 characters.
- **Applicability/presence:** `01-APP`, `02-BRW`, and `03-3RI`; `01-PA` and `02-NPA`; `AReq = C`; required if available unless a market or regional mandate restricts sending it.
- **Privacy/security:** Detailed delivery-location data; apply the same non-normative privacy controls as for Line 1.
- **Citation/Req:** [EMV 3DS v2.2.0, section A.4, p. 186, Table A.1; no local numbered Req]

#### 2.3 Cardholder Shipping Address Line 3 - `shipAddrLine3`

- **Purpose and component responsibility:** Third street-address line or equivalent local portion of the shipping address requested by the Cardholder. Source: 3DS Server.
- **Type/format/values:** String, variable length, maximum 50 characters.
- **Applicability/presence:** `01-APP`, `02-BRW`, and `03-3RI`; `01-PA` and `02-NPA`; `AReq = C`; required if available unless a market or regional mandate restricts sending it.
- **Privacy/security:** Detailed delivery-location data; apply the same non-normative privacy controls as for Lines 1 and 2.
- **Citation/Req:** [EMV 3DS v2.2.0, section A.4, p. 186, Table A.1; no local numbered Req]

#### 2.4 Cardholder Shipping Address Postal Code - `shipAddrPostCode`

- **Purpose and component responsibility:** ZIP or other postal code of the shipping address requested by the Cardholder. Source: 3DS Server.
- **Type/format/values:** String, variable length, maximum 16 characters.
- **Applicability/presence:** `01-APP`, `02-BRW`, and `03-3RI`; `01-PA` and `02-NPA`; `AReq = C`; required if available unless a market or regional mandate restricts sending it.
- **Privacy/security:** Shipping-location data. Issuer ACS observation (non-normative): treat it as privacy-relevant and avoid unnecessary exposure or unsupported normalization.
- **Citation/Req:** [EMV 3DS v2.2.0, section A.4, p. 186, Table A.1; no local numbered Req]

### Physical PDF page 187

#### 2.5 Cardholder Shipping Address State - `shipAddrState`

- **Purpose and component responsibility:** State or province of the shipping address associated with the card used for the purchase. Source: 3DS Server.
- **Type/format/values:** String, variable length, maximum 3 characters; **should** be the country subdivision code defined in ISO 3166-2.
- **Applicability/presence:** `01-APP`, `02-BRW`, and `03-3RI`; `01-PA` and `02-NPA`; `AReq = C`; required if available unless a market/regional mandate restricts sending it or State is not applicable for the country.
- **Privacy/security:** Shipping-location data. Issuer ACS observation (non-normative): preserve the country-applicability exception and do not upgrade the printed `should` into an unstated rejection rule.
- **Citation/Req:** [EMV 3DS v2.2.0, section A.4, p. 187, Table A.1; no local numbered Req]

### Physical PDF page 188

#### 2.6 Cardholder Work Phone Number - `workPhone`

- **Purpose and component responsibility:** Work phone number provided by the Cardholder. Source: 3DS Server.
- **Type/format/values:** Variable length; named parts `cc` (1-3 characters) and `subscriber` (variable, maximum 15 characters), with ITU E.164 referenced for further format and length information. Table A.1 prints `JSON Data Type: String`, but its accepted-value text and example show an object containing `cc` and `subscriber`; this source tension is unresolved.
- **Applicability/presence:** `01-APP`, `02-BRW`, and `03-3RI`; `01-PA` and `02-NPA`; `AReq = C`; required if available unless a market or regional mandate restricts sending it.
- **Privacy/security:** Direct contact data. Issuer ACS observation (non-normative): restrict access and mask/redact logs, analytics, and support views; the row does not say the number is verified or used for an authentication challenge.
- **Citation/Req:** [EMV 3DS v2.2.0, section A.4, p. 188, Table A.1; no local numbered Req]

### Physical PDF page 189

#### 2.7 Challenge Cancelation Indicator - `challengeCancel`

- **Purpose and component responsibility:** Tells the ACS and DS that authentication was canceled. Sources: 3DS SDK and ACS.
- **Type/format/values:** String, exactly 2 characters. `01` Cardholder selected Cancel; `02` reserved for future EMVCo use and invalid until defined; `03` transaction timed out - Decoupled Authentication; `04` transaction timed out at ACS - other timeouts; `05` transaction timed out at ACS - first CReq not received; `06` transaction error; `07` unknown; `08` transaction timed out at SDK; `09-79` reserved for future EMVCo use and invalid until defined; `80-99` reserved for future DS use.
- **Applicability/presence:** `01-APP`, `02-BRW`, and `03-3RI`; `01-PA` and `02-NPA`; `CReq = C` and `RReq = C`. In App CReq, required when user interaction with the cancel button or another indicated reason canceled the transaction. In RReq, required when the ACS identifies cancellation for an indicated reason. Value `04` or `05` is required when Transaction Status Reason is `14`.
- **Privacy/security:** Security-relevant state and failure metadata. The row assigns creation to the SDK for CReq and ACS for RReq but does not define a universal cancellation-recovery or retry policy.
- **Citation/Req:** [EMV 3DS v2.2.0, section A.4, p. 189, Table A.1; no local numbered Req]

### Physical PDF page 190

#### 2.8 Challenge Completion Indicator - `challengeCompletionInd`

- **Purpose and component responsibility:** Conveys the current state of the ACS challenge cycle in every App CRes and whether further challenge messages are required. Source: ACS. If set to `Y`, the ACS will populate Transaction Status in CRes.
- **Type/format/values:** String, exactly 1 character. `Y` = challenge completed and no further challenge-message exchanges required; `N` = challenge not completed and additional exchanges required.
- **Applicability/presence:** `01-APP`; `01-PA` and `02-NPA`; `CRes = R`; no additional conditional text.
- **Privacy/security:** Protocol state-control data. Issuer ACS observation (non-normative): couple it to the ACS transaction state machine and avoid treating `Y` alone as the authentication outcome; the row separately points to Transaction Status.
- **Citation/Req:** [EMV 3DS v2.2.0, section A.4, p. 190, Table A.1; no local numbered Req]

### Physical PDF page 191

#### 2.9 Challenge Data Entry - `challengeDataEntry`

- **Purpose and component responsibility:** Data the Cardholder entered into the Native UI text field. Source: 3DS SDK. ACS UI Type `05` is not supported for this field.
- **Type/format/values:** String, variable length, maximum 45 characters. The printed example is syntactically/mnemonically ambiguous because it prefixes a `challengeDataEntry` example with `challengeSelectInfo`; do not infer a different canonical field name from that example.
- **Applicability/presence:** `01-APP`; `01-PA` and `02-NPA`; `CReq = C`. Required when ACS UI Type is `01`, `02`, or `03`, challenge data has been entered, and Challenge Cancelation Indicator and Resend Challenge Information Code are absent. Table A.14 supplies the missing-field combinations and ACS responses.
- **Privacy/security:** Cardholder-entered authentication data and therefore security-sensitive. Issuer ACS observation (non-normative): prevent disclosure in ordinary logs, analytics, traces, and support views. Table A.1 does not define the authentication secret's semantics or verification algorithm.
- **Citation/Req:** [EMV 3DS v2.2.0, section A.4, p. 191, Table A.1; section A.7.7, p. 252, Table A.14; no local numbered Req]

#### 2.10 Challenge HTML Data Entry - `challengeHTMLDataEntry`

- **Purpose and component responsibility:** Data the Cardholder entered into the HTML UI. Source: 3DS SDK. ACS UI Types `01`, `02`, `03`, and `04` are not supported for this field.
- **Type/format/values:** String, variable length, maximum 256 characters.
- **Applicability/presence:** `01-APP`; `01-PA` and `02-NPA`; `CReq = C`; required when ACS UI Type is `05` and Challenge Cancelation Indicator is absent.
- **Privacy/security:** Potential authentication input and therefore security-sensitive. The row does not define its internal HTML form schema, escaping, secret type, or verification method.
- **Citation/Req:** [EMV 3DS v2.2.0, section A.4, p. 191, Table A.1; no local numbered Req]

### Physical PDF page 192

#### 2.11 Challenge Information Header - `challengeInfoHeader`

- **Purpose and component responsibility:** Header text for the challenge-information screen. Source: ACS.
- **Type/format/values:** String, variable length, maximum 45 characters; if populated, displayed to the Cardholder.
- **Applicability/presence:** `01-APP`; `01-PA` and `02-NPA`; `CRes = O`; no conditional text.
- **Privacy/security:** Cardholder-visible issuer content. Issuer ACS observation (non-normative): use controlled, localized text and avoid secrets, unsafe links, or unnecessary personal data.
- **Citation/Req:** [EMV 3DS v2.2.0, section A.4, p. 192, Table A.1; no local numbered Req]

#### 2.12 Challenge Information Label - `challengeInfoLabel`

- **Purpose and component responsibility:** Issuer-provided label that modifies the Challenge Data Entry field. Source: ACS.
- **Type/format/values:** String, variable length, maximum 45 characters; if populated, displayed to the Cardholder.
- **Applicability/presence:** `01-APP`; `01-PA` and `02-NPA`; `CRes = O`; no conditional text.
- **Privacy/security:** Cardholder-visible instruction metadata. Non-normative ACS observation: keep the label consistent with the actual authentication input and do not include sensitive values.
- **Citation/Req:** [EMV 3DS v2.2.0, section A.4, p. 192, Table A.1; no local numbered Req]

#### 2.13 Challenge Information Text - `challengeInfoText`

- **Purpose and component responsibility:** Text from the ACS/Issuer to the Cardholder during the Challenge Message exchange. Source: ACS.
- **Type/format/values:** String, variable length, maximum 350 characters. If populated, displayed to the Cardholder. Carriage return is supported and represented as `\n`.
- **Applicability/presence:** `01-APP`; `01-PA` and `02-NPA`; `CRes = O`; no conditional text.
- **Privacy/security:** Cardholder-visible issuer content. Non-normative ACS observation: use controlled/localized templates and exclude secrets, unsafe links, and unnecessary account or transaction data.
- **Citation/Req:** [EMV 3DS v2.2.0, section A.4, p. 192, Table A.1; no local numbered Req]

### Physical PDF page 193

#### 2.14 Challenge Information Text Indicator - `challengeInfoTextIndicator`

- **Purpose and component responsibility:** Indicates whether the Issuer/ACS wants a warning icon or similar visual indicator to draw attention to Challenge Information Text. Source: ACS.
- **Type/format/values:** String, length 1. `Y` = display indicator; `N` = do not display indicator. If populated, the information is displayed to the Cardholder.
- **Applicability/presence:** `01-APP`; `01-PA` and `02-NPA`; `CRes = O`; no conditional text.
- **Privacy/security:** UI-attention metadata, not an authentication result. Non-normative ACS observation: do not infer severity levels or security semantics beyond the displayed/not-displayed values.
- **Citation/Req:** [EMV 3DS v2.2.0, section A.4, p. 193, Table A.1; no local numbered Req]

#### 2.15 Challenge No Entry - `challengeNoEntry`

- **Purpose and component responsibility:** Indicates that the Cardholder submitted an empty response. Source: 3DS SDK. If present, it contains `Y`.
- **Type/format/values:** String, exactly 1 character; only `Y` = no data entry.
- **Applicability/presence:** `01-APP`; `01-PA` and `02-NPA`; `CReq = C`. Required when ACS UI Type is `01`, `02`, or `03` and Challenge Data Entry, Challenge Cancelation Indicator, and Resend Challenge Information Code are absent. Table A.14 defines ACS behavior for the missing/present combinations.
- **Privacy/security:** Challenge-state input rather than entered challenge content. It must not be conflated with cancellation, resend, or authentication failure.
- **Citation/Req:** [EMV 3DS v2.2.0, section A.4, p. 193, Table A.1; section A.7.7, p. 252, Table A.14; no local numbered Req]

### Physical PDF page 194

#### 2.16 Challenge Selection Information - `challengeSelectInfo`

- **Purpose and component responsibility:** Single- or multi-select choices presented to the Cardholder; the SDK parses the JSON array for display. Source: ACS.
- **Type/format/values:** Array, variable length; each name/value pair has a maximum of 45 characters. If populated, displayed to the Cardholder. The source example uses masked phone and email choices.
- **Applicability/presence:** `01-APP`; `01-PA` and `02-NPA`; `CRes = O`; no conditional text.
- **Privacy/security:** May contain masked contact/routing choices. Issuer ACS observation (non-normative): preserve masking and avoid placing full contact details, credentials, or internal identifiers in labels or keys.
- **Citation/Req:** [EMV 3DS v2.2.0, section A.4, p. 194, Table A.1; no local numbered Req]

### Physical PDF page 195

#### 2.17 Challenge Window Size - `challengeWindowSize`

- **Purpose and component responsibility:** Dimensions of the Browser challenge window shown to the Cardholder; the ACS shall reply with content formatted to render appropriately in it. Source: 3DS Requestor.
- **Type/format/values:** String, exactly 2 characters. `01` = 250 x 400; `02` = 390 x 400; `03` = 500 x 600; `04` = 600 x 400; `05` = full screen.
- **Applicability/presence:** `02-BRW`; `01-PA` and `02-NPA`; `CReq = R`; no additional conditional text.
- **Privacy/security:** Browser rendering input; not a device fingerprint or security proof by itself. The previously logged Req 177 wording conflict remains unresolved.
- **Citation/Req:** [EMV 3DS v2.2.0, section A.4, p. 195, Table A.1; section 4.3.1.2, p. 103, Req 177]

#### 2.18 Device Channel - `deviceChannel`

- **Purpose and component responsibility:** Identifies the channel interface used to initiate the transaction. Source: 3DS Server.
- **Type/format/values:** String, exactly 2 characters. `01` App-based; `02` Browser; `03` 3DS Requestor Initiated; `04-79` reserved for future EMVCo use and invalid until defined; `80-99` reserved for DS use.
- **Applicability/presence:** `01-APP`, `02-BRW`, and `03-3RI`; `01-PA` and `02-NPA`; `AReq = R`; no conditional text.
- **Privacy/security:** Routing and validation context. It identifies the protocol channel, not the physical device, browser identity, or authentication outcome.
- **Citation/Req:** [EMV 3DS v2.2.0, section A.4, p. 195, Table A.1; no local numbered Req]

### Physical PDF page 196

#### 2.19 Device Information - `deviceInfo`

- **Purpose and component responsibility:** Device information gathered by the 3DS SDK from a Consumer Device. Table A.1 assigns the source as DS because the DS populates the ACS-bound AReq with the decrypted data obtained from SDK Encrypted Data.
- **Type/format/values:** String, variable length, maximum 64000 characters; Base64url-encoded JSON object represented as a string. Values are defined in the external EMV 3-D Secure SDK Specification.
- **Applicability/presence:** `01-APP`; `01-PA` and `02-NPA`; `AReq = C`; required between DS and ACS and absent on the 3DS Server-to-DS AReq leg.
- **Privacy/security:** Explicit security boundary: the SDK encrypts Device Information into SDK Encrypted Data for the DS; the DS decrypts it and places the result in `deviceInfo` unencrypted at field level in the ACS-bound AReq, which remains protected in transit by the applicable secure link. Base64url is encoding, not encryption. [Normative source statement plus bounded interpretation]
- **Citation/Req:** [EMV 3DS v2.2.0, section A.4, p. 196, Table A.1; section A.3, p. 147; no local numbered Req]

#### 2.20 Device Rendering Options Supported - `deviceRenderOptions`

- **Purpose and component responsibility:** Defines the SDK interfaces and UI types the device supports for SDK challenge display. Source: 3DS Server.
- **Type/format/values:** Variable-length JSON object. Table A.13 defines `sdkInterface` (`01` Native, `02` HTML, `03` Both) and `sdkUiType` (array of `01` Text, `02` Single Select, `03` Multi Select, `04` OOB, `05` HTML Other; Native valid `01-04`, HTML valid `01-05`).
- **Applicability/presence:** `01-APP`; `01-PA` and `02-NPA`; `AReq = R`; no additional conditional text.
- **Responsibility/Req:** The row points to Req 314, under which the 3DS SDK shall support all Device Rendering Options. Table A.13 also states that all components must support all Device Rendering Options.
- **Privacy/security:** Device capability data used for safe UI selection, not evidence that a specific challenge method succeeded.
- **Citation/Req:** [EMV 3DS v2.2.0, section A.4, p. 196, Table A.1; section 4.1, p. 80, Req 314; section A.7.6, p. 251, Table A.13]

### Physical PDF page 197

#### 2.21 DS End Protocol Version - `dsEndProtocolVersion`

- **Purpose and component responsibility:** Most recent active protocol version supported by the DS. Source: DS. The row says it is optional within Card Range Data.
- **Type/format/values:** String, variable length, 5-8 characters.
- **Applicability/presence:** Device Channel `N/A`; Message Category `N/A`; Table A.1 prints `PRes = R`. Table A.6 instead marks this nested Card Range Data field `O`; the conflict is unresolved.
- **Privacy/security:** Capability/version metadata. It is not a transaction identifier and does not identify a particular ACS instance.
- **Citation/Req:** [EMV 3DS v2.2.0, section A.4, p. 197, Table A.1; section A.5.7, p. 233, Table A.6; no local numbered Req]

#### 2.22 DS Start Protocol Version - `dsStartProtocolVersion`

- **Purpose and component responsibility:** Source: DS. Table A.1 calls it the **most recent** active version supported by the DS, while Table A.6 calls it the **earliest (oldest)** active version. The row also says it is optional within Card Range Data.
- **Type/format/values:** String, variable length, 5-8 characters.
- **Applicability/presence:** Device Channel `N/A`; Message Category `N/A`; Table A.1 prints `PRes = R`; Table A.6 marks the nested field `O`. Both source conflicts are unresolved.
- **Privacy/security:** Capability/version metadata. Do not silently substitute an implementation definition without authoritative clarification.
- **Citation/Req:** [EMV 3DS v2.2.0, section A.4, p. 197, Table A.1; section A.5.7, p. 233, Table A.6; no local numbered Req]

#### 2.23 DS Reference Number - `dsReferenceNumber`

- **Purpose and component responsibility:** EMVCo-assigned unique identifier used to track an approved DS. Source: DS.
- **Type/format/values:** String, variable length, maximum 32 characters.
- **Applicability/presence:** `01-APP`, `02-BRW`, and `03-3RI`; `01-PA` and `02-NPA`; `AReq = C`, `ARes = R`. The DS populates AReq before passing it to the ACS.
- **Identifier assignment/correlation:** Assigned by EMVCo for an approved DS and populated by the DS. It identifies the DS, not the transaction; the row states no equivalence to `dsTransID` or any other transaction ID.
- **Privacy/security:** Component identity/provenance metadata. Issuer ACS observation (non-normative): validate and audit it as protocol metadata, but do not treat it alone as endpoint authentication.
- **Citation/Req:** [EMV 3DS v2.2.0, section A.4, p. 197, Table A.1; no local numbered Req]

### Physical PDF page 198

#### 2.24 DS Transaction ID - `dsTransID`

- **Purpose and component responsibility:** Universally unique transaction identifier assigned by the DS to identify one transaction. Source/assigning component: DS.
- **Type/format/values:** String, exactly 36 characters; canonical IETF RFC 4122 format. Any RFC 4122 version may be used if the output meets the stated requirements.
- **Applicability/presence:** `01-APP`, `02-BRW`, and `03-3RI`; `01-PA` and `02-NPA`; `AReq = C`, `ARes = R`, `RReq = R`, `RRes = R`, `PRes = R`, `Erro = C`. The DS adds it to AReq before sending to the ACS. In Error Message, required if available, for example obtained from a message or being generated.
- **Identifier assignment/correlation:** The row supports correlation of the listed messages to the single DS-identified transaction. It does not state that `dsTransID` equals, embeds, or can be derived from the 3DS Server, ACS, or SDK Transaction IDs.
- **Privacy/security:** High-value correlation metadata, but not stated to be a secret, authenticator, or authorization token. Issuer ACS observation (non-normative): avoid exposing it unnecessarily and use the authenticated protocol context, not possession of the ID alone, for trust decisions.
- **Citation/Req:** [EMV 3DS v2.2.0, section A.4, p. 198, Table A.1; no local numbered Req]

#### 2.25 DS URL - `dsURL`

- **Purpose and component responsibility:** DS endpoint to which the ACS sends RReq after a challenge. The ACS is expressly responsible for storing the value for later transaction use. Source: DS.
- **Type/format/values:** String, variable length, maximum 2048 characters; fully qualified URL. The row prints an `http://` example but does not use it to replace the separate secure-link requirements.
- **Applicability/presence:** `01-APP`, `02-BRW`, and `03-3RI`; `01-PA` and `02-NPA`; `AReq = C`; required only on the DS-to-ACS AReq leg and absent on the 3DS Server-to-DS leg.
- **Identifier routing/correlation:** DS supplies the transaction route; ACS stores it for the later RReq in that transaction. The row does not define endpoint discovery, failover, or URL-to-DS Reference Number validation.
- **Privacy/security:** Security-sensitive routing configuration. The DS-ACS RReq/RRes link has separate mutual-TLS requirements; the example is not authority to downgrade them.
- **Citation/Req:** [EMV 3DS v2.2.0, section A.4, p. 198, Table A.1; section 6.1.3, pp. 135-136; no local numbered Req]

#### 2.26 Electronic Commerce Indicator (ECI) - `eci`

- **Purpose and component responsibility:** Payment System-specific value from the ACS or DS indicating the result of the attempt to authenticate the Cardholder. Sources: ACS and DS.
- **Type/format/values:** String, exactly 2 characters; values are Payment System-specific.
- **Applicability/presence:** `01-APP`, `02-BRW`, and `03-3RI`; `01-PA` and `02-NPA`; `ARes = C`, `RReq = C`; presence requirements are DS-specific.
- **Privacy/security:** Authentication-result metadata whose values and presence depend on the applicable Payment System/DS. Do not invent a universal ECI mapping from this Core row.
- **Citation/Req:** [EMV 3DS v2.2.0, section A.4, p. 198, Table A.1; no local numbered Req]

### Physical PDF page 199

#### 2.27 Error Code - `errorCode`

- **Purpose and component responsibility:** Code for the problem found in a message. Table A.1 leaves Source blank; no originating component is assigned by this row.
- **Type/format/values:** String, exactly 3 characters; values are defined in Table A.4: `101`, `102`, `103`, `201`, `202`, `203`, `204`, `301`, `302`, `303`, `304`, `305`, `306`, `307`, `402`, `403`, `404`, and `405` with their associated descriptions/details.
- **Applicability/presence:** Device Channel `N/A`; Message Category `N/A`; `Erro = R`; no conditional text.
- **Privacy/security:** Security/operational failure classification. It does not by itself identify the affected transaction; transaction IDs have separate conditional Error Message rows.
- **Citation/Req:** [EMV 3DS v2.2.0, section A.4, p. 199, Table A.1; section A.5.5, pp. 227-230, Table A.4; no local numbered Req]

#### 2.28 Error Component - `errorComponent`

- **Purpose and component responsibility:** Identifies the 3-D Secure component that detected the error. Table A.1 leaves Source blank.
- **Type/format/values:** String, exactly 1 character. `C` = 3DS SDK; `S` = 3DS Server; `D` = DS; `A` = ACS.
- **Applicability/presence:** Device Channel `N/A`; Message Category `N/A`; `Erro = R`; no conditional text.
- **Privacy/security:** Error-origin metadata. It identifies the detecting component type, not necessarily root cause, fault ownership, or a specific deployed instance.
- **Citation/Req:** [EMV 3DS v2.2.0, section A.4, p. 199, Table A.1; no local numbered Req]

#### 2.29 Error Description - `errorDescription`

- **Purpose and component responsibility:** Text describing the identified problem. Table A.1 leaves Source blank.
- **Type/format/values:** String, variable length, maximum 2048 characters. Table A.4 supplies expected descriptions as guidelines associated with each Error Code.
- **Applicability/presence:** Device Channel `N/A`; Message Category `N/A`; `Erro = R`; no conditional text.
- **Privacy/security:** Free text can expose message or system detail. Issuer ACS observation (non-normative): generate controlled text and avoid secrets, PANs, personal data, stack traces, or internal topology not required by the applicable error definition.
- **Citation/Req:** [EMV 3DS v2.2.0, section A.4, p. 199, Table A.1; section A.5.5, pp. 227-230, Table A.4; no local numbered Req]

#### 2.30 Error Detail - `errorDetail`

- **Purpose and component responsibility:** Additional detail about the identified problem. Table A.1 leaves Source blank.
- **Type/format/values:** String, variable length, maximum 2048 characters. Table A.4 supplies code-specific expected content, including element names or extension IDs in relevant cases.
- **Applicability/presence:** Device Channel `N/A`; Message Category `N/A`; `Erro = R`; no conditional text.
- **Privacy/security:** Diagnostic data that may expose names/identifiers. Non-normative ACS observation: include the protocol-required/guideline detail while preventing unrelated secrets, personal data, stack traces, and internal topology from entering the field or logs.
- **Citation/Req:** [EMV 3DS v2.2.0, section A.4, p. 199, Table A.1; section A.5.5, pp. 227-230, Table A.4; no local numbered Req]

#### 2.31 Error Message Type - `errorMessageType`

- **Purpose and component responsibility:** Identifies the Message Type found erroneous. Table A.1 leaves Source blank.
- **Type/format/values:** String, exactly 4 characters; values refer to Message Type: `AReq`, `ARes`, `CReq`, `CRes`, `PReq`, `PRes`, `RReq`, `RRes`, or `Erro`.
- **Applicability/presence:** Device Channel `N/A`; Message Category `N/A`; `Erro = C`; required when the Message Type is recognisable.
- **Privacy/security:** Correlation/classification metadata for error processing, not a transaction identifier or proof of the sender.
- **Citation/Req:** [EMV 3DS v2.2.0, section A.4, p. 199, Table A.1; section A.4, p. 205, Table A.1, Message Type cross-reference; no local numbered Req]

### Physical PDF page 200

#### 2.32 EMV Payment Token Indicator - `payTokenInd`

- **Purpose and component responsibility:** `true` means the transaction was de-tokenised before reaching the ACS. The component in the 3-D Secure domain where de-tokenisation occurs populates it. Sources: 3DS Server and DS.
- **Type/format/values:** Boolean; only `true` is valid when present.
- **Applicability/presence:** `01-APP`, `02-BRW`, and `03-3RI`; `01-PA` and `02-NPA`; `AReq = C`; required if an Account Number was de-tokenised.
- **Privacy/security:** Token-processing provenance. It does not carry the token/PAN itself and does not specify de-tokenisation algorithms, key management, or component selection beyond the location where de-tokenisation occurs.
- **Citation/Req:** [EMV 3DS v2.2.0, section A.4, p. 200, Table A.1; no local numbered Req]

#### 2.33 EMV Payment Token Source - `payTokenSource`

- **Purpose and component responsibility:** Identifies the 3-D Secure-domain system where de-tokenisation occurs; that system populates the field. Sources: 3DS Server and DS.
- **Type/format/values:** String, exactly 2 characters. `01` = 3DS Server; `02` = DS; `03-79` reserved for future EMVCo use and invalid until defined; `80-99` reserved for DS use.
- **Applicability/presence:** `01-APP`, `02-BRW`, and `03-3RI`; `01-PA` and `02-NPA`; `AReq = C`; required when `payTokenInd = true`.
- **Privacy/security:** De-tokenisation provenance metadata. It does not identify a private token service API or authorize de-tokenisation.
- **Citation/Req:** [EMV 3DS v2.2.0, section A.4, p. 200, Table A.1; no local numbered Req]

### Physical PDF page 201

#### 2.34 Expandable Information Label - `expandInfoLabel`

- **Purpose and component responsibility:** Label shown to the Cardholder for Expandable Information Text. Source: ACS.
- **Type/format/values:** String, variable length, maximum 45 characters.
- **Applicability/presence:** `01-APP`; `01-PA` and `02-NPA`; `CRes = O`; no conditional text.
- **Privacy/security:** Cardholder-visible issuer content. Non-normative ACS observation: use controlled/localized labels and do not place credentials or personal data in them.
- **Citation/Req:** [EMV 3DS v2.2.0, section A.4, p. 201, Table A.1; no local numbered Req]

#### 2.35 Expandable Information Text - `expandInfoText`

- **Purpose and component responsibility:** Additional issuer text sent by the ACS for Cardholder display in an expandable field. Source: ACS.
- **Type/format/values:** String, variable length, maximum 256 characters. Carriage return is supported and represented as `\n`.
- **Applicability/presence:** `01-APP`; `01-PA` and `02-NPA`; `CRes = O`; no conditional text.
- **Privacy/security:** Cardholder-visible issuer content. Non-normative ACS observation: use controlled/localized text and exclude secrets, unsafe links, and unnecessary personal or account data.
- **Citation/Req:** [EMV 3DS v2.2.0, section A.4, p. 201, Table A.1; no local numbered Req]

#### 2.36 Instalment Payment Data - `purchaseInstalData`

- **Purpose and component responsibility:** Maximum number of authorisations allowed for instalment payments. Source: 3DS Server.
- **Type/format/values:** String, variable length, maximum 3 characters; numeric value shall be greater than 1. Printed accepted examples are `2`, `02`, and `002`.
- **Applicability/presence:** `01-APP`, `02-BRW`, and `03-3RI`; `01-PA` and `02-NPA`; `AReq = C`; required when Merchant and Cardholder agreed to instalments, meaning 3DS Requestor Authentication Indicator is `03`; omitted when not an instalment-payment authentication.
- **Privacy/security:** Commercial/authentication-context data, not an authorisation approval or schedule. Non-normative ACS observation: preserve leading-zero acceptance and do not infer instalment terms absent from the row.
- **Citation/Req:** [EMV 3DS v2.2.0, section A.4, p. 201, Table A.1; no local numbered Req]

### Physical PDF page 202

#### 2.37 Interaction Counter - `interactionCounter`

- **Purpose and component responsibility:** Number of authentication cycles attempted by the Cardholder. Source: ACS; the ACS is expressly responsible for tracking it.
- **Type/format/values:** String, exactly 2 characters. No value range, initial value, or increment rule is stated in this row.
- **Applicability/presence:** `01-APP` and `02-BRW`; `01-PA` and `02-NPA`; `RReq = R`; no conditional text.
- **Privacy/security:** Security-relevant attempt-count metadata. Issuer ACS observation (non-normative): make tracking consistent with the transaction state machine, but do not invent a maximum or padding rule from this row.
- **Citation/Req:** [EMV 3DS v2.2.0, section A.4, p. 202, Table A.1; no local numbered Req]

#### 2.38 Issuer Image - `issuerImage`

- **Purpose and component responsibility:** Sent by the ACS in the initial CRes to give the SDK Issuer logo/image URLs for Native UI. Source: ACS.
- **Type/format/values:** JSON object. Table A.16 defines up to three fully qualified URL strings named `medium`, `high`, and `extraHigh`, each with a maximum of 2048 characters.
- **Applicability/presence:** `01-APP`; `01-PA` and `02-NPA`; `CRes = C`; presence is Payment System-specific.
- **Privacy/security:** Remote-content routing/branding data. Non-normative ACS observation: manage URLs as controlled configuration; the row and Table A.16 do not define image integrity, hosting, caching, or content-validation rules.
- **Citation/Req:** [EMV 3DS v2.2.0, section A.4, p. 202, Table A.1; section A.7.9, p. 255, Table A.16; no local numbered Req]

#### 2.39 Merchant Category Code - `mcc`

- **Purpose and component responsibility:** DS-specific code describing the Merchant's business, product, or service. Source: 3DS Server.
- **Type/format/values:** String, exactly 4 characters; correlates to the Merchant Category Code defined by the applicable Payment System or DS.
- **Applicability/presence:** `01-APP`, `02-BRW`, and `03-3RI`; for `01-PA`, `AReq = R`; for `02-NPA`, `AReq = O`, optional but strongly recommended when the Merchant is also the 3DS Requestor.
- **Privacy/security:** Merchant classification/risk input. Values and validation are scheme/DS-dependent; Table A.4 defines Error Code `306` for an MCC invalid for the Payment System.
- **Citation/Req:** [EMV 3DS v2.2.0, section A.4, p. 202, Table A.1; section A.5.5, p. 229, Table A.4; no local numbered Req]

### Physical PDF page 203

#### 2.40 Merchant Country Code - `merchantCountryCode`

- **Purpose and component responsibility:** Merchant country code, correlated to the definition used by the applicable Payment System or DS. Source: 3DS Server.
- **Type/format/values:** String, exactly 3 characters; ISO 3166-1 numeric three-digit country code excluding Table A.5 values. The same value must be used in the authorisation request.
- **Applicability/presence:** `01-APP`, `02-BRW`, and `03-3RI`; for `01-PA`, `AReq = R`; for `02-NPA`, `AReq = O`, optional but strongly recommended when the Merchant is also the 3DS Requestor.
- **Privacy/security:** Merchant location and cross-message consistency data. Table A.5 excludes ISO 3166-1 values `901-999`; Error Code `304` covers invalid/excluded ISO codes.
- **Citation/Req:** [EMV 3DS v2.2.0, section A.4, p. 203, Table A.1; section A.5.6, p. 231, Table A.5; section A.5.5, p. 229, Table A.4; no local numbered Req]

#### 2.41 Merchant Name - `merchantName`

- **Purpose and component responsibility:** Merchant name assigned by the Acquirer or Payment System. Source: 3DS Server.
- **Type/format/values:** String, variable length, maximum 40 characters; same name used in the ISO 8583 authorisation message.
- **Applicability/presence:** `01-APP`, `02-BRW`, and `03-3RI`; for `01-PA`, `AReq = R`; for `02-NPA`, `AReq = O`, optional but strongly recommended when the Merchant is also the 3DS Requestor.
- **Privacy/security:** Merchant identity and cross-message consistency data. It is not a cryptographic merchant identity or proof of Acquirer assignment by itself.
- **Citation/Req:** [EMV 3DS v2.2.0, section A.4, p. 203, Table A.1; no local numbered Req]

#### 2.42 Merchant Risk Indicator - `merchantRiskIndicator`

- **Purpose and component responsibility:** Merchant assessment of fraud risk for the specific Cardholder and authentication. Source: 3DS Server.
- **Type/format/values:** Variable-length JSON object; Table A.9 defines optional `deliveryEmailAddress`, `deliveryTimeframe`, `giftCardAmount`, `giftCardCount`, `giftCardCurr`, `preOrderDate`, `preOrderPurchaseInd`, `reorderItemsInd`, and `shipIndicator` fields and their formats/values. Table A.1's note says "Device Merchant Risk Indicator," but the canonical row and Table A.9 use Merchant Risk Indicator.
- **Applicability/presence:** `01-APP`, `02-BRW`, and `03-3RI`; `01-PA` and `02-NPA`; `AReq = O`; optional but strongly recommended.
- **Privacy/security:** Risk and purchase-context data that may include delivery email, delivery timing, gift-card, preorder, reorder, and shipping-method information. Issuer ACS observation (non-normative): treat the object as privacy- and fraud-sensitive and validate only the edits actually specified.
- **Citation/Req:** [EMV 3DS v2.2.0, section A.4, p. 203, Table A.1; section A.7.2, pp. 243-245, Table A.9; no local numbered Req]

### Physical PDF page 204

#### 2.43 Message Category - `messageCategory`

- **Purpose and component responsibility:** Identifies the message category for the use case. Source: 3DS Server.
- **Type/format/values:** String, exactly 2 characters. `01` = Payment Authentication (PA); `02` = Non-Payment Authentication (NPA); `03-79` reserved for future EMVCo use and invalid until defined; `80-99` reserved for DS use.
- **Applicability/presence:** `01-APP`, `02-BRW`, and `03-3RI`; `01-PA` and `02-NPA`; `AReq = R`, `RReq = R`; no conditional text.
- **Privacy/security:** Transaction/use-case classification controlling interpretation of other presence rules. It is not an authentication outcome or payment authorization status.
- **Citation/Req:** [EMV 3DS v2.2.0, section A.4, p. 204, Table A.1; no local numbered Req]

#### 2.44 Message Extension - `messageExtension`

- **Purpose and component responsibility:** Carries data needed for requirements not otherwise defined in the Core message. Table A.1 prints Source: 3DS Server and says conditions are set by each DS; section A.6 separately assigns the extension-data format to the party defining the extension.
- **Type/format/values:** Array, variable length, maximum 81920 characters; Table A.7 defines required attributes `criticalityIndicator` (Boolean), `data` (Object, maximum 8059 characters), `id` (String, maximum 64 characters, Payment System RID prefix required), and `name` (String, maximum 64 characters). Section A.6 supports at most 10 extension objects in the array.
- **Applicability/presence:** `01-APP`, `02-BRW`, and `03-3RI`; `01-PA` and `02-NPA`; `AReq = C`, `ARes = C`, `CReq = C`, `CRes = C`, `PReq = C`, `PRes = C`, `RReq = C`, and `RRes = C`; conditions are set by each DS.
- **Extension handling:** Each extension must have a unique identifier. When criticality is `true`, the recipient must recognise and process the extension; an unrecognised critical extension makes the message invalid and requires Error Code `202`. An unrecognised non-critical extension must be ignored by a recipient that cannot recognise it and passed onward unaltered. All critical Message Extensions shall be assigned by EMVCo.
- **Ownership/correlation:** The `id` identifies the extension, not the 3DS transaction. Section A.6 allows EMVCo-, OID-, URI-, and DS-assigned IDs and says the defining party sets identifier format/value. It does not define an extension-level transaction correlation scheme.
- **Privacy/security:** Extension payload semantics and sensitivity depend on the registered/defined extension. Issuer ACS observation (non-normative): treat unknown content as untrusted structured data, enforce the normative size/count/criticality rules, and avoid logging opaque payloads by default.
- **Citation/Req:** [EMV 3DS v2.2.0, section A.4, p. 204, Table A.1; sections A.6-A.6.3, pp. 234-237, Table A.7; section A.5.5, p. 228, Table A.4; no local numbered Req]

## 3. ACS behavior traceability

| ACS behavior area | Material Part 3 elements | Source-prescribed effect | Evidence |
|---|---|---|---|
| Shipping and Cardholder contact input | `shipAddrLine1`, `shipAddrLine2`, `shipAddrLine3`, `shipAddrPostCode`, `shipAddrState`, `workPhone` | Conditional AReq data subject to availability, market/regional restrictions, and state applicability; state only **should** follow ISO 3166-2. | [EMV 3DS v2.2.0, section A.4, pp. 186-188, Table A.1; no local numbered Reqs] |
| App challenge input, state, and display | `challengeCancel`, `challengeCompletionInd`, `challengeDataEntry`, `challengeHTMLDataEntry`, `challengeInfoHeader`, `challengeInfoLabel`, `challengeInfoText`, `challengeInfoTextIndicator`, `challengeNoEntry`, `challengeSelectInfo`, `expandInfoLabel`, `expandInfoText`, `interactionCounter`, `issuerImage` | SDK supplies conditional CReq actions/data; ACS supplies required challenge-state CRes and optional/conditional display data, tracks interactions, and supplies required RReq count. Table A.14 governs missing Native challenge input combinations. | [EMV 3DS v2.2.0, section A.4, pp. 189-194 and 201-202, Table A.1; section A.7.7, p. 252, Table A.14; section A.7.9, p. 255, Table A.16; no local numbered Reqs] |
| Browser challenge rendering | `challengeWindowSize` | Requestor sends required Browser CReq window size; ACS shall format its content for that window. | [EMV 3DS v2.2.0, section A.4, p. 195, Table A.1; no local numbered Req] |
| App device capability and information | `deviceChannel`, `deviceInfo`, `deviceRenderOptions` | AReq identifies the channel; DS converts SDK-encrypted Device Information into the ACS-bound field; required rendering capability object is constrained by Table A.13 and Req 314. | [EMV 3DS v2.2.0, section A.3, p. 147; section A.4, pp. 195-196, Table A.1; section 4.1, p. 80, Req 314; section A.7.6, p. 251, Table A.13] |
| DS capability, identity, transaction correlation, and result route | `dsEndProtocolVersion`, `dsStartProtocolVersion`, `dsReferenceNumber`, `dsTransID`, `dsURL`, `eci` | DS supplies version/identity metadata, assigns the transaction ID, injects ACS-bound route/correlation data, and shares ECI responsibility under DS-specific rules. ACS must store `dsURL` for later RReq. | [EMV 3DS v2.2.0, section A.4, pp. 197-198, Table A.1; section A.5.7, pp. 232-233, Table A.6; no local numbered Reqs] |
| Error payload and classification | `errorCode`, `errorComponent`, `errorDescription`, `errorDetail`, `errorMessageType` | Error Message requires code, detecting-component code, description, and detail; erroneous Message Type is conditional on recognition. Table A.4 supplies code-specific semantics and expected content. | [EMV 3DS v2.2.0, section A.4, p. 199, Table A.1; section A.5.5, pp. 227-230, Table A.4; no local numbered Reqs] |
| Payment-token and merchant/risk context | `payTokenInd`, `payTokenSource`, `purchaseInstalData`, `mcc`, `merchantCountryCode`, `merchantName`, `merchantRiskIndicator` | AReq communicates de-tokenisation location, instalment context, merchant identity/classification, and optional merchant risk data under category- and scheme-specific rules. | [EMV 3DS v2.2.0, section A.4, pp. 200-203, Table A.1; section A.5.6, p. 231, Table A.5; section A.7.2, pp. 243-245, Table A.9; no local numbered Reqs] |
| Message classification and extensions | `messageCategory`, `messageExtension` | Category is required in AReq/RReq. Extensions are DS-conditioned across the eight non-Error message types and require registered structure, size/count enforcement, and critical/non-critical handling. | [EMV 3DS v2.2.0, section A.4, p. 204, Table A.1; sections A.6-A.6.3, pp. 234-237, Table A.7; no local numbered Reqs] |

## 4. Issuer ACS implementation observations

The following are non-normative implementation considerations, not additional EMV 3DS requirements:

- Treat challenge input (`challengeDataEntry` and `challengeHTMLDataEntry`), Cardholder contact/location data, device information, transaction identifiers, error detail, and risk information as distinct sensitive-data classes for access, logging, analytics, support, retention, and deletion. The Table A.1 rows do not define those internal controls. [Protocol anchors: EMV 3DS v2.2.0, section A.4, pp. 186-204, Table A.1]
- Maintain separate identifiers for the approved DS (`dsReferenceNumber`), the DS-assigned transaction (`dsTransID`), and extension registration (`messageExtension[].id`). The source does not define them as aliases or derivable values. [Protocol anchors: EMV 3DS v2.2.0, section A.4, pp. 197-198 and 204, Table A.1; section A.6.1, p. 236, Table A.7]
- Preserve the message-leg boundaries for `deviceInfo` and `dsURL`: each is present only on the DS-to-ACS AReq leg, not the 3DS Server-to-DS leg. [Protocol anchors: EMV 3DS v2.2.0, section A.4, pp. 196 and 198, Table A.1]
- Implement extension processing with explicit registries/allowlists, size/count limits, criticality behavior, and safe parsing. The registry mechanics, extension-specific schema validation, and internal storage/logging design remain implementation or scheme decisions. [Protocol anchors: EMV 3DS v2.2.0, sections A.6-A.6.3, pp. 234-237, Table A.7]

## 5. Unresolved items in this batch

- **Work Phone JSON type conflicts with its structure.** Table A.1 prints `JSON Data Type: String`, while the accepted-value text and example represent `workPhone` as an object with `cc` and `subscriber`. Obtain authoritative clarification and align this field with the applicable message schema/SDK or DS profile instead of silently choosing one representation. [EMV 3DS v2.2.0, section A.4, p. 188, Table A.1]
- **Challenge Data Entry example is malformed or mislabeled.** The canonical row and field name are `challengeDataEntry`, but the example includes a standalone `challengeSelectInfo:` prefix before the `challengeDataEntry` name/value. Do not use the example to alter the canonical field or infer an unprinted nesting relationship. [EMV 3DS v2.2.0, section A.4, p. 191, Table A.1]
- **DS Start Protocol Version conflicts with Table A.6.** Table A.1 calls `dsStartProtocolVersion` the most recent active version, while Table A.6 calls it the earliest/oldest. Obtain authoritative clarification; the Start/End naming and Table A.6 support the latter interpretation, but that is an interpretation rather than a correction to Table A.1. [EMV 3DS v2.2.0, section A.4, p. 197, Table A.1; section A.5.7, p. 233, Table A.6]
- **DS protocol-version inclusion conflicts across tables.** Table A.1 marks both `dsStartProtocolVersion` and `dsEndProtocolVersion` `PRes = R` while saying they are optional within Card Range Data; Table A.6 marks both nested fields `O`, and section A.5.7 says the Card Range Data may optionally contain them. Preserve the container/nested distinction and obtain authoritative validation guidance before rejecting a PRes/card-range object for their absence. [EMV 3DS v2.2.0, section A.4, p. 197, Table A.1; section A.5.7, pp. 232-233, Table A.6]
- **Message Extension source responsibility is not fully aligned with its routes.** Table A.1 lists only 3DS Server as Source even though it permits the element conditionally in request and response messages created by multiple components. Section A.6 assigns format and identifier choices to the extension-defining party and Table A.1 assigns conditions to each DS, but neither passage reconciles the single Source cell for all routes. Use the applicable registered extension/DS rules rather than inferring universal 3DS Server authorship. [EMV 3DS v2.2.0, section A.4, p. 204, Table A.1; sections A.6-A.6.3, pp. 234-237, Table A.7]

## 6. Verification record

- Physical PDF pages 186-204 were rendered and directly inspected at readable resolution; physical pages 185 and 205 were checked as adjacent boundaries.
- The row sequence was compared with `extracted/EMVCo_3DS_Spec_v220_122018-layout.txt`. Count: 44 complete rows.
- First scoped row: physical page 186 begins **Cardholder Shipping Address Line 1** / `shipAddrLine1`.
- Last scoped row: physical page 204 ends **Message Extension** / `messageExtension`.
- Physical page 205 begins **Message Type** / `messageType`, confirming that Message Extension is complete and the next row is outside scope.
- No scoped Table A.1 row continues across a physical-page boundary. Page 189 lacks the repeated header but contains the complete Challenge Cancelation Indicator row; this was visually checked rather than inferred from extraction.
- Followed internal cross-references were visually checked on physical pages 80 (Req 314), 147 (AReq encryption boundary), 227-231 (Tables A.4-A.5), 232-233 (Table A.6), 234-237 (section A.6 and Table A.7), 243-245 (Table A.9), 251 (Table A.13), 252 (Table A.14), and 255 (Table A.16). Physical page 205 was also checked for the Message Type cross-reference and next-row boundary.
- Except for referenced Req 314, no local numbered requirement appears in the scoped Table A.1 rows or followed Annex cross-references. Citations therefore use section, physical PDF page, table, and `no local numbered Req` rather than inventing requirement numbers.

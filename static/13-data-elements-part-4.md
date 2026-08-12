# EMV 3DS v2.2.0 Data Elements - Part 4

## 1. Scope and source rules

This batch captures every Table A.1 row from **Message Type** (`messageType`) on physical PDF page 205 through **Why Information Text** (`whyInfoText`) on physical PDF page 222. The scoped range contains 35 rows. Physical page 204 ends the preceding batch with Message Extension, and page 223 starts section A.5 rather than another Table A.1 row. [EMV 3DS v2.2.0, section A.4, pp. 204-223, Table A.1; no local numbered Req]

The Annex A `R`, `C`, and `O` inclusion rules, missing-field behavior, and edit criteria documented in Part 1 remain applicable. A `C` cell preserves the condition printed in the row and is not equivalent to always required; a source omission or conflict is not repaired by inference. [EMV 3DS v2.2.0, Annex A and sections A.1-A.2, pp. 145-147; no local numbered Req]

Each entry records the canonical data-element and JSON field names, purpose, source/component responsibility, type and length/format, values or referenced value table, channel/category and message presence, conditionality, and a bounded privacy/security assessment. Unless expressly stated, privacy/security observations are non-normative issuer ACS implementation considerations, not additional protocol requirements. Table A.1 supplies no local numbered requirements on pages 205-222; related numbered requirements are cited only where they materially clarify the row. [EMV 3DS v2.2.0, section A.4, pp. 205-222, Table A.1; no local numbered Reqs]

## 2. Data-element register

### Physical PDF page 205

#### 2.1 Message Type - `messageType`

- **Purpose and component responsibility:** Identifies the type of message passed. Sources: 3DS Server, 3DS SDK, DS, and ACS; the applicable message creator supplies it.
- **Type/format/values:** String, exactly 4 characters. Accepted values: `AReq`, `ARes`, `CReq`, `CRes`, `PReq`, `PRes`, `RReq`, `RRes`, and `Erro`.
- **Applicability/presence:** `01-APP`, `02-BRW`, and `03-3RI`; `01-PA` and `02-NPA`; required in every listed message: `AReq = R`, `ARes = R`, `CReq = R`, `CRes = R`, `PReq = R`, `PRes = R`, `RReq = R`, `RRes = R`, and `Erro = R`.
- **Privacy/security:** Protocol dispatch metadata. It is not a transaction identifier, sender authenticator, or result, and receivers still apply the message/phase/channel validation rules.
- **Citation/Req:** [EMV 3DS v2.2.0, section A.4, p. 205, Table A.1; no local numbered Req]

#### 2.2 Message Version Number - `messageVersion`

- **Purpose and component responsibility:** Protocol version of the specification used by the system creating the message. The 3DS Server sets it when originating the transaction in AReq, and it does not change during that 3DS transaction. Source: 3DS Server.
- **Type/format/values:** String, variable length, 5-8 characters; values refer to Table 1.5. In this edition, Table 1.5 marks `2.0.0` Deprecated and `2.1.0` and `2.2.0` Active.
- **Applicability/presence:** `01-APP`, `02-BRW`, and `03-3RI`; `01-PA` and `02-NPA`; required in `AReq`, `ARes`, `CReq`, `CRes`, `PReq`, `PRes`, `RReq`, `RRes`, and `Erro`.
- **Privacy/security:** Version/state-consistency metadata used in validation and correlation. It is not negotiated anew per message and is not evidence of peer authenticity. The existing Table 1.3/document-version conflict remains unresolved.
- **Citation/Req:** [EMV 3DS v2.2.0, section A.4, p. 205, Table A.1; section 1.7, p. 27, Table 1.5; section 5.1.4, p. 112, Reqs 195 and 311]

### Physical PDF page 206

#### 2.3 Notification URL - `notificationURL`

- **Purpose and component responsibility:** Fully qualified URL of the system receiving the Browser final CRes or Error Message. The 3DS Server supplies it in AReq; after challenge completion and receipt of RRes, the ACS invokes the Cardholder browser to post the final CRes to this URL.
- **Type/format/values:** String, variable length, maximum 256 characters; fully qualified URL.
- **Applicability/presence:** `02-BRW`; `01-PA` and `02-NPA`; `AReq = R`; no additional conditional text.
- **Privacy/security:** Security-sensitive browser return route. The row does not make the URL an authenticator, define an allowlist/origin policy, or require connection reuse. Separate Browser-Requestor secure-link requirements apply, and any query data remains visible to systems handling the URL.
- **Citation/Req:** [EMV 3DS v2.2.0, section A.4, p. 206, Table A.1; section 3.3, pp. 66 and 70, Reqs 117 and 140; section 5.8.2, p. 123, Req 270]

#### 2.4 OOB App Label - `oobAppLabel`

- **Purpose and component responsibility:** Label intended for the link to the OOB App URL, for example, “Click here to open Your Bank App.” Table A.1 prints Source `N/A`.
- **Type/format/values:** String, variable length, maximum 45 characters.
- **Applicability/presence:** `01-APP`; `01-PA` and `02-NPA`; `CRes = O`. Despite that optional cell, the row says this element is reserved for future OOB-flow enhancements: an ACS will not provide it and an SDK will neither process nor display it in this specification version. Table A.18 marks it not displayed for every current Native UI type.
- **Privacy/security:** Future Cardholder-visible text. Implementations must not activate behavior from this row in v2.2.0; if a later profile enables it, text/content controls would be external to this version.
- **Citation/Req:** [EMV 3DS v2.2.0, section A.4, p. 206, Table A.1; section A.8, p. 257, Table A.18; no local numbered Req]

### Physical PDF page 207

#### 2.5 OOB App URL - `oobAppURL`

- **Purpose and component responsibility:** Mobile deep link intended to open the appropriate location in an authentication app used for OOB authentication. Table A.1 prints Source `N/A`.
- **Type/format/values:** String, variable length, maximum 256 characters; accepted value is a fully qualified URL.
- **Applicability/presence:** `01-APP`; `01-PA` and `02-NPA`; `CRes = O`. The row says the element supports future OOB-flow enhancements: an ACS will not provide it and an SDK will not process it in this specification version.
- **Privacy/security:** Future security-sensitive application-routing data. No current-v2.2.0 deep-link validation, scheme allowlist, app binding, or anti-phishing behavior may be inferred from the row.
- **Citation/Req:** [EMV 3DS v2.2.0, section A.4, p. 207, Table A.1; no local numbered Req]

#### 2.6 OOB Continuation Indicator - `oobContinue`

- **Purpose and component responsibility:** Notifies the ACS that the Cardholder completed the requested OOB authentication by selecting Continue. Source: 3DS SDK.
- **Type/format/values:** Boolean; when present, the only valid value is `true`.
- **Applicability/presence:** `01-APP`; `01-PA` and `02-NPA`; `CReq = C`; required for ACS UI Type `04` when the Cardholder selected that option on the device.
- **Privacy/security:** OOB challenge-state signal, not proof that the external authentication succeeded. The ACS must correlate it with its transaction/OOB result state; the private OOB result interface remains outside the protocol.
- **Citation/Req:** [EMV 3DS v2.2.0, section A.4, p. 207, Table A.1; section 3.2, pp. 57-58; no new numbered OOB Req]

### Physical PDF page 208

#### 2.7 OOB Continuation Label - `oobContinueLabel`

- **Purpose and component responsibility:** ACS-provided label for the UI button the Cardholder selects after completing OOB authentication. Source: ACS.
- **Type/format/values:** String, variable length, maximum 45 characters.
- **Applicability/presence:** `01-APP`; `01-PA` and `02-NPA`; `CRes = C`; required for ACS UI Type `04` when the Cardholder has selected that option. If present, either Challenge Information Header or Challenge Information Text must also be present. Table A.18 places it in zone 3, display order 6, for OOB only.
- **Privacy/security:** Cardholder-visible issuer instruction. Non-normative ACS consideration: localize it, keep it consistent with the actual continuation behavior, and exclude secrets or deceptive instructions.
- **Citation/Req:** [EMV 3DS v2.2.0, section A.4, p. 208, Table A.1; section A.8, p. 257, Table A.18; no local numbered Req]

#### 2.8 Payment System Image - `psImage`

- **Purpose and component responsibility:** Sent by the ACS in the initial CRes to provide DS or Payment System logo/image URLs for Native UI. Source: ACS.
- **Type/format/values:** JSON object; Table A.17 permits up to three fully qualified URL strings named `medium`, `high`, and `extraHigh`, each with a variable length up to 2048 characters.
- **Applicability/presence:** `01-APP`; `01-PA` and `02-NPA`; `CRes = C`; presence is Payment System-specific. Table A.18 places it in zone 2, display order 1, for all four Native UI types.
- **Privacy/security:** Remote branding-content routing. Non-normative ACS consideration: manage URLs as controlled scheme configuration; neither row defines content integrity, hosting, cache, fetch, or image-validation policy.
- **Citation/Req:** [EMV 3DS v2.2.0, section A.4, p. 208, Table A.1; section A.7.10, p. 256, Table A.17; section A.8, p. 257, Table A.18; no local numbered Req]

### Physical PDF page 209

#### 2.9 Purchase Amount - `purchaseAmount`

- **Purpose and component responsibility:** Purchase amount in minor currency units with punctuation removed; Purchase Currency Exponent permits reconstruction of punctuation. Source: 3DS Server.
- **Type/format/values:** String, variable length, maximum 48 characters. For USD 123.45, accepted examples are `12345`, `012345`, and `0012345`; the row does not prohibit the shown leading zeros.
- **Applicability/presence:** `01-APP`, `02-BRW`, and `03-3RI`; `01-PA` and `02-NPA`. For `01-PA`, `AReq = R`. For `02-NPA`, `AReq = C`, required if 3DS Requestor Authentication Indicator is `02` (Recurring) or `03` (Instalment).
- **Privacy/security:** Financial transaction data and risk input. Non-normative ACS consideration: restrict exposure in logs/analytics and validate it together with currency/exponent without converting the string in a way that loses permitted leading zeros.
- **Citation/Req:** [EMV 3DS v2.2.0, section A.4, p. 209, Table A.1; section A.4, p. 150, Table A.1; no local numbered Req]

#### 2.10 Purchase Currency - `purchaseCurrency`

- **Purpose and component responsibility:** Currency in which Purchase Amount is expressed. Source: 3DS Server.
- **Type/format/values:** String, exactly 3 numeric characters; ISO 4217 three-digit currency code excluding the values in Table A.5.
- **Applicability/presence:** `01-APP`, `02-BRW`, and `03-3RI`; `01-PA` and `02-NPA`. For `01-PA`, `AReq = R`; for `02-NPA`, `AReq = C`, required if 3DS Requestor Authentication Indicator is `02` or `03`.
- **Privacy/security:** Financial transaction context. It must remain paired consistently with amount/exponent; the row does not define exchange-rate conversion or settlement currency.
- **Citation/Req:** [EMV 3DS v2.2.0, section A.4, p. 209, Table A.1; section A.5.6, p. 231, Table A.5; no local numbered Req]

#### 2.11 Purchase Currency Exponent - `purchaseExponent`

- **Purpose and component responsibility:** Number of minor currency units specified by the ISO 4217 exponent. Source: 3DS Server.
- **Type/format/values:** String, exactly 1 character. Printed examples: `2` for USD and `0` for Yen.
- **Applicability/presence:** `01-APP`, `02-BRW`, and `03-3RI`; `01-PA` and `02-NPA`. For `01-PA`, `AReq = R`; for `02-NPA`, `AReq = C`, required if 3DS Requestor Authentication Indicator is `02` or `03`.
- **Privacy/security:** Financial-format metadata. It is not a currency code, amount, or authorization result; use it only with the corresponding currency and amount.
- **Citation/Req:** [EMV 3DS v2.2.0, section A.4, p. 209, Table A.1; no local numbered Req]

### Physical PDF page 210

#### 2.12 Purchase Date & Time - `purchaseDate`

- **Purpose and component responsibility:** Purchase date and time expressed in UTC. Source: 3DS Server.
- **Type/format/values:** String, exactly 14 characters; `YYYYMMDDHHMMSS`.
- **Applicability/presence:** `01-APP`, `02-BRW`, and `03-3RI`; `01-PA` and `02-NPA`. For `01-PA`, `AReq = R`; for `02-NPA`, `AReq = C`, required if 3DS Requestor Authentication Indicator is `02` or `03`.
- **Privacy/security:** Transaction timing and correlation data. The row mandates UTC and a lexical format but does not define clock synchronization, tolerance, or freshness rejection.
- **Citation/Req:** [EMV 3DS v2.2.0, section A.4, p. 210, Table A.1; no local numbered Req]

#### 2.13 Recurring Expiry - `recurringExpiry`

- **Purpose and component responsibility:** Date after which no further authorizations shall be performed. Source: 3DS Server.
- **Type/format/values:** String, exactly 8 characters; `YYYYMMDD`.
- **Applicability/presence:** `01-APP`, `02-BRW`, and `03-3RI`; `01-PA` and `02-NPA`; `AReq = C`; required if 3DS Requestor Authentication Indicator is `02` (Recurring) or `03` (Instalment).
- **Privacy/security:** Commercial schedule/authentication context. It does not itself authorize future payments, define credential-on-file status, or select the `03-3RI` device channel.
- **Citation/Req:** [EMV 3DS v2.2.0, section A.4, p. 210, Table A.1; section A.4, p. 150, Table A.1; no local numbered Req]

#### 2.14 Recurring Frequency - `recurringFrequency`

- **Purpose and component responsibility:** Minimum number of days between authorizations. Source: 3DS Server.
- **Type/format/values:** String, variable length, maximum 4 characters. Accepted examples are `31`, `031`, and `0031`; leading-zero forms are explicitly shown.
- **Applicability/presence:** `01-APP`, `02-BRW`, and `03-3RI`; `01-PA` and `02-NPA`; `AReq = C`; required if 3DS Requestor Authentication Indicator is `02` or `03`.
- **Privacy/security:** Commercial schedule/risk context. The row gives neither a numeric range nor retry/authorization behavior and does not make recurring or instalment synonymous with 3RI.
- **Citation/Req:** [EMV 3DS v2.2.0, section A.4, p. 210, Table A.1; no local numbered Req]

#### 2.15 Resend Challenge Information Code - `resendChallenge`

- **Purpose and component responsibility:** Requests that the ACS resend challenge information to the Cardholder. Source: 3DS SDK.
- **Type/format/values:** String, exactly 1 character: `Y` Resend; `N` Do not Resend.
- **Applicability/presence:** `01-APP`; `01-PA` and `02-NPA`; `CReq = C`; required for Native UI when the Cardholder requests resend. The challenge-input missing-field combinations are separately governed by Table A.14.
- **Privacy/security:** Challenge action/control metadata. It does not define delivery channel, resend limit, rate limit, or whether a new authentication secret must be generated.
- **Citation/Req:** [EMV 3DS v2.2.0, section A.4, p. 210, Table A.1; section A.7.7, p. 252, Table A.14; no local numbered Req]

### Physical PDF page 211

#### 2.16 Resend Information Label - `resendInformationLabel`

- **Purpose and component responsibility:** Label for the Native UI button used to request that authentication information be resent. Source: ACS.
- **Type/format/values:** String, variable length, maximum 45 characters.
- **Applicability/presence:** `01-APP`; `01-PA` and `02-NPA`; `CRes = C`; required for Native UI if the ACS permits the Cardholder to request resend. Table A.18 places it in zone 3, display order 8, for Text/OTP only.
- **Privacy/security:** Cardholder-visible action label. Non-normative ACS consideration: localize it and align it with the actual resend policy without exposing delivery details or secrets.
- **Citation/Req:** [EMV 3DS v2.2.0, section A.4, p. 211, Table A.1; section A.8, p. 257, Table A.18; no local numbered Req]

#### 2.17 Results Message Status - `resultsStatus`

- **Purpose and component responsibility:** Required RRes status supplied by the 3DS Server to report whether the RReq was received for further processing or why challenge processing could not continue. Source: 3DS Server. Table A.1 incorrectly describes the Results Request as being “from the 3DS Server”; the RReq route is ACS -> DS -> 3DS Server and RRes returns the status.
- **Type/format/values:** String, exactly 2 characters. `01` RReq received for further processing; `02` CReq not sent to ACS by the 3DS Requestor (3DS Server or Requestor opted out of challenge); `03` ARes with Transaction Status `C` or `D` was not delivered to the Requestor because of technical error; `04-79` reserved for future EMVCo use and invalid until defined; `80-99` reserved for DS use.
- **Applicability/presence:** `01-APP`, `02-BRW`, and `03-3RI`; `01-PA` and `02-NPA`; `RRes = R`; no conditional text. Table B.9 confirms it in the RRes data set.
- **Privacy/security:** Result-delivery and operational-state metadata. It is not the authentication outcome (`transStatus`) and should not be mapped to one without an explicit rule.
- **Citation/Req:** [EMV 3DS v2.2.0, section A.4, p. 211, Table A.1; section B.9, p. 269, Table B.9; section 3.1, pp. 54-56, Reqs 62-75]

### Physical PDF page 212

#### 2.18 SDK App ID - `sdkAppID`

- **Purpose and component responsibility:** Universally unique identifier generated and stored by the SDK for each installation of the 3DS Requestor App on a Consumer Device. Source: 3DS SDK, sent via 3DS Server.
- **Type/format/values:** String, exactly 36 characters; canonical IETF RFC 4122 format, with any RFC 4122 version permitted if the output meets the stated requirements.
- **Applicability/presence:** `01-APP`; `01-PA` and `02-NPA`; `AReq = R`; no conditional text.
- **Privacy/security:** Persistent installation-level identifier with device/app linkability relevance. It is not the per-transaction SDK Transaction ID or an authentication secret.
- **Citation/Req:** [EMV 3DS v2.2.0, section A.4, p. 212, Table A.1; section 3.1, pp. 45-46, Reqs 2 and 4]

#### 2.19 SDK Counter SDK to ACS - `sdkCounterStoA`

- **Purpose and component responsibility:** SDK-to-ACS counter used as a security measure in the direct App secure channel. Source: 3DS SDK.
- **Type/format/values:** String, exactly 3 characters. The row states no lexical value range; Chapter 6 requires the receiver to compare it to its local counter, increment it, and stop with an error on mismatch or wrap to zero.
- **Applicability/presence:** `01-APP`; `01-PA` and `02-NPA`; `CReq = R`; no conditional text.
- **Privacy/security:** Explicit sequencing/freshness control for protected App CReq messages. It is not a universal replay counter for other message routes.
- **Citation/Req:** [EMV 3DS v2.2.0, section A.4, p. 212, Table A.1; sections 6.2.4.1 and 6.2.4.3, pp. 143-144; no numbered Chapter 6 Req]

#### 2.20 SDK Encrypted Data - `sdkEncData`

- **Purpose and component responsibility:** JWE object represented as a string, containing Device Information encrypted by the SDK for the DS to decrypt. Source: 3DS SDK, sent via 3DS Server. The row calls it the only encrypted **field** in this specification version; direct App CReq/CRes are separately protected as whole JWE messages.
- **Type/format/values:** String, variable length, maximum 64000 characters; JWE as defined in section 6.2.2.1.
- **Applicability/presence:** `01-APP`; `01-PA` and `02-NPA`; `AReq = C`; required on the 3DS Server-to-DS leg and absent on the DS-to-ACS leg, where the DS instead supplies decrypted Device Information.
- **Privacy/security:** Explicit field-level confidentiality boundary: the 3DS Server carries but cannot read the protected Device Information, the DS decrypts it, and the ACS receives it as normal AReq data over the secure DS-ACS link.
- **Citation/Req:** [EMV 3DS v2.2.0, section A.4, p. 212, Table A.1; sections 6.2.2-6.2.2.2, pp. 138-140; section A.3, p. 147; no local numbered Req]

### Physical PDF page 213

#### 2.21 SDK Ephemeral Public Key (QC) - `sdkEphemPubKey`

- **Purpose and component responsibility:** Public component of the ephemeral key pair generated by the SDK and used to establish SDK-ACS session keys. Source: 3DS SDK. It is a standalone object in AReq and is included inside ACS Signed Content in ARes.
- **Type/format/values:** JSON object/JWK, variable length, maximum 256 characters. The row does not define which object serialization is measured for the character limit.
- **Applicability/presence:** `01-APP`; `01-PA` and `02-NPA`; `AReq = R`; ARes inclusion is through ACS Signed Content rather than as a separate top-level row value.
- **Privacy/security:** Public key-establishment data, not a secret key. Chapter 6 binds it into ACS Signed Content and uses it for ECDH-ES; ephemeral private material and derived keys remain outside the field.
- **Citation/Req:** [EMV 3DS v2.2.0, section A.4, p. 213, Table A.1; sections 6.2.3.1-6.2.3.3, pp. 141-143; no numbered Chapter 6 Req]

#### 2.22 SDK Maximum Timeout - `sdkMaxTimeout`

- **Purpose and component responsibility:** Maximum time, in minutes, for all exchanges. Source: 3DS SDK.
- **Type/format/values:** String, exactly 2 characters; accepted value is greater than or equal to `05`. The row states no separate maximum beyond the field edit.
- **Applicability/presence:** `01-APP`; `01-PA` and `02-NPA`; `AReq = R`; no conditional text.
- **Privacy/security:** Availability/state-lifetime parameter, not an authentication timeout outcome by itself. Issuer ACS consideration: preserve the negotiated transaction deadline consistently across nodes; storage/clock design is not specified by the row.
- **Citation/Req:** [EMV 3DS v2.2.0, section A.4, p. 213, Table A.1; section 5.5.2.2, p. 118, Reqs 237, 239, and 312]

#### 2.23 SDK Reference Number - `sdkReferenceNumber`

- **Purpose and component responsibility:** Identifies the vendor and version of the SDK integrated into the Requestor App; assigned by EMVCo when the SDK is approved. Source: 3DS SDK, sent via 3DS Server.
- **Type/format/values:** String, variable length, maximum 32 characters.
- **Applicability/presence:** `01-APP`; `01-PA` and `02-NPA`; `AReq = R`; no conditional text.
- **Privacy/security:** Approved-component identity/version metadata and a secure-channel derivation input. It is not a transaction ID, certificate, or proof that the running SDK binary is genuine by possession alone.
- **Citation/Req:** [EMV 3DS v2.2.0, section A.4, p. 213, Table A.1; section 6.2.3.2, p. 141; no numbered Chapter 6 Req]

### Physical PDF page 214

#### 2.24 SDK Transaction ID - `sdkTransID`

- **Purpose and component responsibility:** Universally unique identifier assigned by the SDK to identify one transaction. Source/assigning component: 3DS SDK, sent via 3DS Server where applicable.
- **Type/format/values:** String, exactly 36 characters; canonical IETF RFC 4122 format, with any RFC 4122 version permitted if the output meets the requirements.
- **Applicability/presence:** `01-APP`; `01-PA` and `02-NPA`; required in `AReq`, `ARes`, `CReq`, `CRes`, `RReq`, and `RRes`; `Erro = C`, required when available, for example obtained from a message or the message being generated.
- **Privacy/security:** High-value per-transaction correlation metadata, not a secret or authorization token. It is distinct from the installation-level `sdkAppID` and from 3DS Server, DS, and ACS Transaction IDs; no derivation/equality is stated.
- **Citation/Req:** [EMV 3DS v2.2.0, section A.4, p. 214, Table A.1; section 3.1, p. 46, Reqs 3-5; no local numbered Req]

#### 2.25 Serial Number - `serialNum`

- **Purpose and component responsibility:** In PReq, asks the DS for Card Range Data updated since the referenced PRes; absent means return all ranges. In PRes, identifies the current state of Card Range Data using a DS-meaningful value that the 3DS Server should retain for a later incremental PReq. Sources: DS and 3DS Server.
- **Type/format/values:** String, variable length, maximum 20 alphanumeric characters.
- **Applicability/presence:** Device Channel `N/A`; Message Category `N/A`; `PReq = O`, `PRes = O`; no conditional text.
- **Privacy/security:** Preparation-cache state token, not a transaction identifier or ordered number with client-defined semantics. The DS controls its meaning; the 3DS Server should treat it as opaque.
- **Citation/Req:** [EMV 3DS v2.2.0, section A.4, p. 214, Table A.1; section 5.6, pp. 119-121, Reqs 246-251, 303-304, and 385]

### Physical PDF page 215

#### 2.26 Submit Authentication Label - `submitAuthenticationLabel`

- **Purpose and component responsibility:** ACS-provided label for the button the Cardholder selects after completing authentication; explicitly not used for OOB authentication. Source: ACS.
- **Type/format/values:** String, variable length, maximum 45 characters.
- **Applicability/presence:** `01-APP`; `01-PA` and `02-NPA`; `CRes = C`; required when ACS UI Type is `01`, `02`, or `03`. If present, at least one of Challenge Information Header, Challenge Information Label, or Challenge Information Text must also be present. Table A.18 places it in zone 3, display order 7, for Text/OTP, Single Select, and Multi Select, but not OOB.
- **Privacy/security:** Cardholder-visible action label. Non-normative ACS consideration: keep it consistent with the actual submission action and avoid implying a result before verification.
- **Citation/Req:** [EMV 3DS v2.2.0, section A.4, p. 215, Table A.1; section A.8, p. 258, Table A.18; no local numbered Req]

### Physical PDF pages 216-217 (row spans both pages)

#### 2.27 Transaction Status - `transStatus`

- **Purpose and component responsibility:** Indicates whether the transaction qualifies as an authenticated transaction or account verification. Sources: ACS and DS.
- **Type/format/values:** String, exactly 1 character. `Y` successful authentication/account verification; `N` not authenticated/not verified and denied; `U` could not be performed because of technical or other problem; `A` attempts processing with proof of attempt but not authentication/verification; `C` challenge required through CReq/CRes; `D` challenge required with Decoupled Authentication confirmed; `R` issuer rejects authentication/verification and requests no authorization attempt; `I` informational only, acknowledging Requestor challenge preference.
- **Applicability/presence:** `01-APP`, `02-BRW`, and `03-3RI`; `01-PA` and `02-NPA`. For `01-PA`, `ARes = R`, `RReq = R`, and final `CRes = R`. For `02-NPA`, those three cells are `C`, with conditions defined by the DS. Final CRes may contain only `Y` or `N`. If 3DS Requestor Challenge Indicator is `06` (no challenge requested; data share only), `C` is invalid.
- **Value-table constraints:** Table A.15 makes `Y` and `N` valid in ARes, final CRes, and RReq; `U`, `A`, and `R` valid in ARes/RReq but invalid in final CRes; and `C`, `D`, and `I` valid only in ARes. Its footnotes make `C` invalid when Device Channel in AReq is `03`, permit `D` only when 3DS Requestor Decoupled Request Indicator in AReq is `Y`, and permit `I` only when 3DS Requestor Challenge Indicator in AReq is `05`, `06`, or `07`. Invalid-message combinations follow Table A.15's error/end-processing directions.
- **Privacy/security:** Authentication decision/status metadata with authorization-impacting meaning, but not payment authorization itself. Issuer ACS consideration: couple generation to an auditable state machine and do not collapse `A`, `Y`, `N`, `U`, and `R` into a generic success/failure flag.
- **Citation/Req:** [EMV 3DS v2.2.0, section A.4, pp. 216-217, Table A.1; section A.7.8, pp. 253-254, Table A.15; section 3.4, pp. 74-76, Reqs 287-299 and 328]

### Physical PDF pages 218-220 (row spans all three pages)

#### 2.28 Transaction Status Reason - `transStatusReason`

- **Purpose and component responsibility:** Explains why Transaction Status has its value. Sources: ACS and DS.
- **Type/format/values:** String, exactly 2 characters. `01` card authentication failed; `02` unknown device; `03` unsupported device; `04` exceeds authentication frequency limit; `05` expired card; `06` invalid card number; `07` invalid transaction; `08` no card record; `09` security failure; `10` stolen card; `11` suspected fraud; `12` transaction not permitted to Cardholder; `13` Cardholder not enrolled; `14` transaction timed out at ACS; `15` low confidence; `16` medium confidence; `17` high confidence; `18` very high confidence; `19` exceeds ACS maximum challenges; `20` non-payment transaction not supported; `21` 3RI transaction not supported; `22` ACS technical issue; `23` Decoupled Authentication required by ACS but not requested; `24` Requestor Decoupled Max Expiry Time exceeded; `25` insufficient Decoupled time, so ACS will not attempt; `26` authentication attempted but not performed by Cardholder; `27-79` reserved for future EMVCo use and invalid until defined; `80-99` reserved for DS use.
- **Applicability/presence:** `01-APP`, `02-BRW`, and `03-3RI`; `01-PA` and `02-NPA`; `ARes = C`, `RReq = C`. For `01-PA`, required when Transaction Status is `N`, `U`, or `R`; for `02-NPA`, conditional as defined by the DS.
- **Privacy/security:** Security/risk/failure classification that can expose sensitive fraud or account context. Issuer ACS consideration: restrict unnecessary display/logging, keep it consistent with `transStatus`, and do not invent a universal DS mapping for reserved or DS-defined values.
- **Citation/Req:** [EMV 3DS v2.2.0, section A.4, pp. 218-220, Table A.1; section 3.4, pp. 72-75 and 77, Reqs 271, 280-283, 296, and 339]

### Physical PDF page 220

#### 2.29 Transaction Type - `transType`

- **Purpose and component responsibility:** Identifies the type of transaction being authenticated. Source: 3DS Server.
- **Type/format/values:** String, exactly 2 characters. `01` Goods/Service Purchase; `03` Check Acceptance; `10` Account Funding; `11` Quasi-Cash Transaction; `28` Prepaid Activation and Load. The row says values are derived from ISO 8583.
- **Applicability/presence:** `01-APP`, `02-BRW`, and `03-3RI`; `01-PA` only; `AReq = C`; required in some markets, with Merchants in Brazil given as an example, and otherwise optional.
- **Privacy/security:** Transaction-purpose/risk classification. Market/scheme applicability is external to the Core row; do not invent additional codes or a universal mandate.
- **Citation/Req:** [EMV 3DS v2.2.0, section A.4, p. 220, Table A.1; no local numbered Req]

#### 2.30 Whitelisting Data Entry - `whitelistingDataEntry`

- **Purpose and component responsibility:** SDK indicator to the ACS confirming whether the Cardholder opted for whitelisting/trusted-beneficiary treatment. Source: SDK.
- **Type/format/values:** String, exactly 1 character: `Y` Whitelisting Confirmed; `N` Whitelisting Not Confirmed.
- **Applicability/presence:** `01-APP`; `01-PA` and `02-NPA`; `CReq = C`; if Whitelisting Information Text was present in CRes, the SDK must return this field in CReq.
- **Privacy/security:** Cardholder preference/consent-like security state. It is not by itself the final whitelist status; the ACS/DS/3DS Server communicate that separately through `whiteListStatus` and its source.
- **Citation/Req:** [EMV 3DS v2.2.0, section A.4, p. 220, Table A.1; no local numbered Req]

### Physical PDF page 221

#### 2.31 Whitelisting Information Text - `whitelistingInfoText`

- **Purpose and component responsibility:** ACS/Issuer text shown to the Cardholder during a whitelisting transaction, for example asking whether to add the Merchant to a whitelist. Source: ACS.
- **Type/format/values:** Variable length, maximum 64 characters. Table A.1 does **not** print a JSON Data Type for this row; this analysis does not infer one. Table A.18 identifies the field as a UI data element.
- **Applicability/presence:** `01-APP`; `01-PA` and `02-NPA`; `CRes = O`; if present, the SDK must display it. Table A.18 places it in zone 3, display order 9, for all four Native UI types.
- **Privacy/security:** Cardholder-visible trusted-beneficiary prompt. Non-normative ACS consideration: state the choice clearly, localize it, avoid deceptive consent language, and exclude unnecessary personal/account data.
- **Citation/Req:** [EMV 3DS v2.2.0, section A.4, p. 221, Table A.1; section A.8, p. 258, Table A.18; no local numbered Req]

#### 2.32 Whitelist Status - `whiteListStatus`

- **Purpose and component responsibility:** Communicates trusted-beneficiary/whitelist status among the ACS, DS, and 3DS Requestor. Sources: 3DS Server, DS, and ACS.
- **Type/format/values:** String, exactly 1 character. `Y` Requestor whitelisted by Cardholder; `N` not whitelisted; `E` issuer determines not eligible; `P` pending Cardholder confirmation; `R` Cardholder rejected; `U` unknown, unavailable, or not applicable. In AReq, only `Y` and `N` are valid.
- **Applicability/presence:** `01-APP`, `02-BRW`, and `03-3RI`; `01-PA` and `02-NPA`; `AReq = O`, `ARes = O`, `RReq = O`; no conditional text.
- **Privacy/security:** Cardholder preference/issuer decision and risk metadata. The value is not an authorization credential and its source must remain distinguishable through `whiteListStatusSource` when present.
- **Citation/Req:** [EMV 3DS v2.2.0, section A.4, p. 221, Table A.1; section B.8, p. 268, Table B.8; no local numbered Req]

### Physical PDF page 222

#### 2.33 Whitelist Status Source - `whiteListStatusSource`

- **Purpose and component responsibility:** Identifies the system that set Whitelist Status; the setting system populates this field. Sources: 3DS Server, DS, and ACS.
- **Type/format/values:** String, exactly 2 characters. `01` 3DS Server; `02` DS; `03` ACS; `04-79` reserved for future EMVCo use and invalid until defined; `80-99` reserved for DS use.
- **Applicability/presence:** `01-APP`, `02-BRW`, and `03-3RI`; `01-PA` and `02-NPA`; `AReq = C`, `ARes = C`, `RReq = C`; required if Whitelist Status is present.
- **Privacy/security:** Decision-provenance metadata. It identifies a component type, not a particular deployed instance, signer, or proof that the status is trustworthy outside the authenticated message route.
- **Citation/Req:** [EMV 3DS v2.2.0, section A.4, p. 222, Table A.1; section B.8, p. 268, Table B.8; no local numbered Req]

#### 2.34 Why Information Label - `whyInfoLabel`

- **Purpose and component responsibility:** ACS-provided label for the Cardholder-visible “why” information section. Source: ACS.
- **Type/format/values:** String, variable length, maximum 45 characters.
- **Applicability/presence:** `01-APP`; `01-PA` and `02-NPA`; `CRes = O`; no conditional text. Table A.18 places it in zone 4, display order 10, for all four Native UI types.
- **Privacy/security:** Optional explanatory UI label. Non-normative ACS consideration: use controlled/localized text that accurately introduces the explanation without exposing secrets or internal risk logic.
- **Citation/Req:** [EMV 3DS v2.2.0, section A.4, p. 222, Table A.1; section A.8, p. 258, Table A.18; no local numbered Req]

#### 2.35 Why Information Text - `whyInfoText`

- **Purpose and component responsibility:** Issuer text displayed to explain why the Cardholder is being asked to perform the authentication task. Source: ACS.
- **Type/format/values:** String, variable length, maximum 256 characters; carriage return is supported and represented as `\n`.
- **Applicability/presence:** `01-APP`; `01-PA` and `02-NPA`; `CRes = O`; no conditional text. Table A.18 places it in zone 4, display order 11, for all four Native UI types.
- **Privacy/security:** Cardholder-visible explanatory content. Non-normative ACS consideration: use accurate, localized templates; do not disclose secrets, internal fraud rules, unsafe links, or unnecessary personal/account data.
- **Citation/Req:** [EMV 3DS v2.2.0, section A.4, p. 222, Table A.1; section A.8, p. 258, Table A.18; no local numbered Req]

## 3. ACS behavior traceability

| ACS behavior area | Material Part 4 elements | Source-prescribed effect | Evidence |
|---|---|---|---|
| Message identity and version consistency | `messageType`, `messageVersion` | Every protocol message carries both fields; the Server-originated version remains unchanged for the transaction and refers to Table 1.5. | [EMV 3DS v2.2.0, section A.4, p. 205, Table A.1; section 1.7, p. 27, Table 1.5; section 5.1.4, p. 112, Reqs 195 and 311] |
| Browser result return | `notificationURL` | Required Browser AReq route for final CRes/Error return; after RRes, ACS uses the browser to post final CRes to this URL. | [EMV 3DS v2.2.0, section A.4, p. 206, Table A.1; section 3.3, pp. 66 and 70, Reqs 117 and 140; section 5.8.2, p. 123, Req 270] |
| App OOB and Native UI controls | `oobAppLabel`, `oobAppURL`, `oobContinue`, `oobContinueLabel`, `psImage`, `resendChallenge`, `resendInformationLabel`, `submitAuthenticationLabel`, `whitelistingDataEntry`, `whitelistingInfoText`, `whyInfoLabel`, `whyInfoText` | Current OOB continuation uses SDK CReq/ACS CRes state fields; the App label/URL are future-only. ACS supplies conditional/optional display content under the UI-type and companion-field rules, and Table A.18 fixes Native placement. | [EMV 3DS v2.2.0, section A.4, pp. 206-208, 210-211, 215, and 220-222, Table A.1; section A.8, pp. 257-258, Table A.18; no local numbered Reqs] |
| Purchase and recurring context | `purchaseAmount`, `purchaseCurrency`, `purchaseExponent`, `purchaseDate`, `recurringExpiry`, `recurringFrequency`, `transType` | PA amount/currency/exponent/date are required; NPA inclusion is conditional on recurring/instalment indicator. Recurring expiry/frequency share that condition, and transaction type is market-conditional. | [EMV 3DS v2.2.0, section A.4, pp. 209-210 and 220, Table A.1; no local numbered Reqs] |
| RReq/RRes result processing | `resultsStatus`, `transStatus`, `transStatusReason`, `sdkTransID`, `whiteListStatus`, `whiteListStatusSource` | RReq carries result, reason when required, transaction IDs, and optional whitelist state/provenance; RRes carries required `resultsStatus`. Tables B.8/B.9 confirm the message membership while Table A.15 constrains status values by message. | [EMV 3DS v2.2.0, section A.4, pp. 211, 214, and 216-222, Table A.1; section A.7.8, pp. 253-254, Table A.15; sections B.8-B.9, pp. 268-269, Tables B.8-B.9; no local numbered Reqs] |
| App SDK identity and protected channels | `sdkAppID`, `sdkCounterStoA`, `sdkEncData`, `sdkEphemPubKey`, `sdkMaxTimeout`, `sdkReferenceNumber`, `sdkTransID` | SDK supplies installation/transaction/vendor identity, DS-encrypted Device Information, ephemeral key setup, required CReq counter, and timeout. Chapter 6 supplies the cryptographic and counter behavior not repeated in Table A.1. | [EMV 3DS v2.2.0, section A.4, pp. 212-214, Table A.1; sections 6.2.2-6.2.4, pp. 138-144; no numbered Chapter 6 Reqs] |
| Preparation delta state | `serialNum` | 3DS Server may send retained opaque DS state in PReq for incremental Card Range Data; omission requests the full data set. | [EMV 3DS v2.2.0, section A.4, p. 214, Table A.1; section 5.6, pp. 119-121, Reqs 246-251, 303-304, and 385] |

## 4. Issuer ACS implementation observations

These are non-normative implementation considerations, not additional EMV 3DS requirements:

- Treat `notificationURL` and future OOB deep links as untrusted routing inputs unless validated under the applicable channel/scheme policy. The Core row does not define allowlists, origin binding, redirect handling, or application-link verification. [Protocol anchors: EMV 3DS v2.2.0, section A.4, pp. 206-207, Table A.1]
- Keep `resultsStatus` separate from `transStatus`: the former reports RReq/RRes handling, while the latter is the authentication/account-verification result. Do not overwrite a completed authentication outcome merely because result delivery or challenge initiation failed unless a cited rule requires it. [Protocol anchors: EMV 3DS v2.2.0, section A.4, pp. 211 and 216-220, Table A.1]
- Store SDK App ID, SDK Transaction ID, SDK Reference Number, and the four protocol transaction IDs as separate identifiers with distinct sources/assigning components and lifecycles. The specification does not define them as aliases or derivable values. [Protocol anchors: EMV 3DS v2.2.0, section A.4, pp. 157, 164, 198, 212-214, Table A.1]
- Protect purchase values, Cardholder whitelist choices/status, failure reasons, and persistent SDK installation identifiers according to their different privacy/fraud sensitivity. The Table A.1 rows do not prescribe internal logging, access, retention, or deletion controls. [Protocol anchors: EMV 3DS v2.2.0, section A.4, pp. 209-210 and 212-222, Table A.1]
- Generate Cardholder-visible OOB, resend, submit, whitelisting, and “why” content through controlled localized templates. The source defines field limits, presence, and display placement but not accessibility, locale negotiation, translation fallback, or content governance. [Protocol anchors: EMV 3DS v2.2.0, section A.4, pp. 206-208, 210-211, 215, and 220-222, Table A.1; section A.8, pp. 257-258, Table A.18]

## 5. Unresolved and externally dependent items in this batch

1. **3RI versus recurring/instalment condition.** Purchase Amount/Currency/Exponent/Date and Recurring Expiry/Frequency include `03-3RI`, and their NPA conditions refer to 3DS Requestor Authentication Indicator `02` or `03`. The indicator's own Table A.1 row applies only to `01-APP` and `02-BRW`. Authoritative clarification is required for how the condition is evaluated in `03-3RI` NPA AReq. [Unresolved source ambiguity] [EMV 3DS v2.2.0, section A.4, pp. 150 and 209-210, Table A.1]
2. **Results Message Status direction wording.** The row calls the Results Request a message “from the 3DS Server,” while the protocol route sends RReq to the 3DS Server and returns RRes from it. Treat `resultsStatus` as the required RRes field shown by Table A.1/B.9; do not reverse the RReq route. [Unresolved source wording] [EMV 3DS v2.2.0, section A.4, p. 211, Table A.1; section B.9, p. 269, Table B.9; section 3.1, pp. 54-56, Reqs 62-75]
3. **SDK Ephemeral Public Key character limit.** Table A.1 declares a JSON Object/JWK with a maximum of 256 characters but does not define serialization or the representation against which characters are counted. This mirrors the existing ACS public-key object issue. [Unresolved validation basis] [EMV 3DS v2.2.0, section A.4, p. 213, Table A.1]
4. **Whitelisting Information Text omits a JSON type.** The row supplies a 64-character maximum but no JSON Data Type. Preserve the omission and obtain the applicable schema/profile rather than inferring the type solely from the word “Text” or nearby rows. [Unresolved source omission] [EMV 3DS v2.2.0, section A.4, p. 221, Table A.1]
5. **Protocol version defect remains.** The `messageVersion` row correctly points to Table 1.5, where `2.2.0` is Active, but Table 1.3 elsewhere still says the Protocol Version for this specification is `2.1.0`. [Existing unresolved source defect] [EMV 3DS v2.2.0, sections 1.5 and 1.7, pp. 25 and 27; section A.4, p. 205, Table A.1]
6. **Market/scheme and DS rules remain external.** Payment System Image presence, NPA Transaction Status/Reason conditions, Transaction Type market mandates, DS-reserved values, and trusted-beneficiary programme behavior require the applicable scheme/DS/market rules. [Scheme or Vendor Dependency] [EMV 3DS v2.2.0, section A.4, pp. 208, 216-222, Table A.1]
7. **OOB App Label and URL are not current processing fields.** Although their message cells are optional, their rows explicitly prohibit ACS provision and SDK processing/display in this specification version. Any activation requires a later authoritative specification/profile. [Future-version dependency] [EMV 3DS v2.2.0, section A.4, pp. 206-207, Table A.1]

## 6. Boundary and page-break verification

- **First boundary:** Physical page 205 visually starts this batch with Message Type; Message Extension is the preceding row on page 204. Both Message Type and Message Version Number were checked against all nine required message-presence cells. [EMV 3DS v2.2.0, section A.4, pp. 204-205, Table A.1]
- **Internal spanning row 1:** Transaction Status begins on page 216 and its `R` and `I` value text completes on page 217. The row was read as one element and cross-checked against both pages of Table A.15. [EMV 3DS v2.2.0, section A.4, pp. 216-217, Table A.1; section A.7.8, pp. 253-254, Table A.15]
- **Internal spanning row 2:** Transaction Status Reason begins on page 218, continues with values `17-79` on page 219, and completes with `80-99` on page 220 before Transaction Type begins. [EMV 3DS v2.2.0, section A.4, pp. 218-220, Table A.1]
- **Last boundary:** Why Information Text is complete on physical page 222, including its 256-character limit, `\n` carriage-return representation, App/category cells, and optional CRes presence. Physical page 223 begins section A.5 Browser Information, confirming no Table A.1 row was carried into the next batch. [EMV 3DS v2.2.0, section A.4, p. 222, Table A.1; section A.5, p. 223]
- **Supplemental verification:** Table 1.5 (p. 27), Transaction Status Conditions (pp. 253-254), Payment System Image (p. 256), UI Data Elements (pp. 257-258), and RReq/RRes message lists (pp. 268-269) were visually checked for the cross-references used above.

All 18 scoped physical pages (205-222) and all 35 rows were visually inspected against the source PDF and reconciled with the project extraction. No local numbered requirements are printed in the scoped Table A.1 rows; related requirements are cited only for version handling, Browser return, results routing, SDK security, timeouts, and preparation-cache behavior.

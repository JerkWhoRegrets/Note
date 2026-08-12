# EMV 3DS v2.2.0 Data Elements - Part 1

## 1. Scope and source rules

This batch covers Annex A introductory rules and every Table A.1 row on physical PDF pages 145-166. The captured range begins with **3DS Method Completion Indicator** (`threeDSCompInd`) and ends with **Address Match Indicator** (`addrMatch`). Physical page 166 also contains **ACS URL** (`acsURL`). The next row, Authentication Method, begins on physical page 167 and is outside this batch.

Table A.1 uses these columns: canonical data-element name and field name, description, source component, length/format/values, device channel, message category, message inclusion, and conditional inclusion. The source defines one "character" in a length criterion as one UTF-8 character. [EMV 3DS v2.2.0, Annex A, p. 145]

The channel codes are `01-APP` (App-based Authentication), `02-BRW` (Browser-based Authentication), and `03-3RI` (Deviceless Payment Authentication/Verification of Account). The message-category codes are `01-PA` (Payment Authentication) and `02-NPA` (Non-Payment Authentication). [EMV 3DS v2.2.0, Annex A, p. 145]

The inclusion codes are normative:

- `R` - the sender shall include the element for the identified message, channel, and category; the recipient shall check presence and validate content.
- `C` - the sender shall include it when the stated condition is met; the recipient shall check presence and validate content. If the condition is not met and there is no data to send, the element should be absent.
- `O` - the sender may include it; the recipient shall validate it when present. If there is no data to send, the element should be absent.

When a field is required for the transaction's channel and category, it must be present and must not be empty or null. Conditional Inclusion states the condition that the source component is responsible for satisfying. [EMV 3DS v2.2.0, Annex A, pp. 145-146]

### 1.1 Missing required fields

A field is missing when its name/value pair is absent or when its name is present but the value is empty or null. Unless expressly stated otherwise, a receiving component returns an Error Message under section A.5.5 with the applicable Error Component and Error Code `201` when an always-required or conditionally required field is missing. [Normative Requirement; no local numbered Req] [EMV 3DS v2.2.0, section A.1, p. 147]

### 1.2 Field edit criteria

Only validations specified in Annex A are to be performed; a message is not to be rejected for a validation that is not listed in the Annex A tables. If a present field fails the Table A.1 edit criteria, the receiving component returns an Error Message under section A.5.5 with the applicable Error Component and Error Code `203`. [Normative Requirement; no local numbered Req] [EMV 3DS v2.2.0, section A.2, p. 147]

### 1.3 AReq encryption boundary

Ordinary AReq fields are included without field-level encryption and are protected in transit by the secure links in section 6.1. Device Information is the exception described here: the 3DS SDK encrypts it, the 3DS Server sends the resulting JWE as the `3DS SDK Encrypted Data` field to the DS, and only the SDK and DS process that JWE. The DS places the decrypted result into the AReq sent to the ACS as the unencrypted `Device Information` element, still protected in transit by the relevant secure link. This rule does not make Base64url-encoded fields encrypted. [Normative Requirement plus interpretation limited to the stated boundary; no local numbered Req] [EMV 3DS v2.2.0, section A.3, p. 147]

## 2. Data-element register

Each entry preserves the Table A.1 source component, applicable channel/category, message presence, and conditional text. "Privacy/security" distinguishes what the source expressly says from a bounded interpretation; an absence of a source statement is not a claim that an element is harmless or public.

### Physical PDF page 148

#### 2.1 3DS Method Completion Indicator - `threeDSCompInd`

- **Purpose and responsibility:** Indicates whether the 3DS Method successfully completed. Source: 3DS Server. The row does not state a receiver; the value is carried in AReq.
- **Type/format/values:** String, exactly 1 character. `Y` = successfully completed; `N` = did not successfully complete; `U` = unavailable because the 3DS Method URL was not present in PRes data for the card range associated with the Cardholder Account Number.
- **Applicability/presence:** `02-BRW`; `01-PA` and `02-NPA`; `AReq = R`; no additional conditional-inclusion text.
- **Privacy/security:** Completion/capability signal used in authentication processing; the row states no confidentiality or encryption property.
- **Citation:** [EMV 3DS v2.2.0, section A.4, p. 148, Table A.1; no local numbered Req]

### Physical PDF page 149

#### 2.2 3DS Requestor App URL - `threeDSRequestorAppURL`

- **Purpose and responsibility:** Merchant App URL declared in CReq so the Authentication app can call the Merchant app after OOB authentication. Each transaction is to use a unique Transaction ID by using the SDK Transaction ID. Source: 3DS SDK.
- **Type/format/values:** String, variable length, maximum 256 characters; fully qualified URL. Example: `merchantScheme://appName?transID=b2385523-a66c-4907-ac3c-91848e8c0067`.
- **Applicability/presence:** `01-APP`; `01-PA` and `02-NPA`; `CReq = O`; no additional conditional-inclusion text.
- **Privacy/security:** Callback/deep-link routing data containing transaction correlation in the example. The row states no authentication, confidentiality, or encryption rule for the URL.
- **Citation:** [EMV 3DS v2.2.0, section A.4, p. 149, Table A.1; no local numbered Req]

### Physical PDF page 150

#### 2.3 3DS Requestor Authentication Indicator - `threeDSRequestorAuthenticationInd`

- **Purpose and responsibility:** Identifies the type of authentication request and gives the ACS information to determine the best approach for handling it. Source: 3DS Server; stated consumer: ACS.
- **Type/format/values:** String, exactly 2 characters. `01` Payment transaction; `02` Recurring transaction; `03` Instalment transaction; `04` Add card; `05` Maintain card; `06` Cardholder verification as part of EMV token ID&V; `07-79` reserved for EMVCo future use and invalid until defined; `80-99` reserved for DS use.
- **Applicability/presence:** `01-APP` and `02-BRW`; `01-PA` and `02-NPA`; `AReq = R`; no additional conditional-inclusion text.
- **Privacy/security:** ACS decision input describing transaction/authentication purpose; the row states no protection classification.
- **Citation:** [EMV 3DS v2.2.0, section A.4, p. 150, Table A.1; no local numbered Req]

### Physical PDF page 151

#### 2.4 3DS Requestor Authentication Information - `threeDSRequestorAuthenticationInfo`

- **Purpose and responsibility:** Information about how the 3DS Requestor authenticated the Cardholder before or during the transaction. Source: 3DS Server.
- **Type/format/values:** Object, variable length. Refer to Table A.10 for included elements. The data is formatted as a JSON object before placement in this message field.
- **Applicability/presence:** `01-APP` and `02-BRW`; `01-PA` and `02-NPA`; `AReq = O`; Conditional Inclusion says "Optional, recommended to include."
- **Privacy/security:** Authentication-history/context data about a Cardholder is security- and privacy-relevant. The row does not state field-level encryption or a data-handling policy.
- **Citation:** [EMV 3DS v2.2.0, section A.4, p. 151, Table A.1; no local numbered Req]

#### 2.5 3DS Requestor Authentication Method Verification Indicator - `threeDSReqAuthMethodInd`

- **Purpose and responsibility:** Represents DS signature verification performed on the mechanism, for example FIDO, used by the Cardholder to authenticate to the 3DS Requestor. Source: DS.
- **Type/format/values:** String, exactly 2 characters. `01` Verified; `02` Failed; `03` Not Performed; `04-79` reserved for EMVCo future use and invalid until defined; `80-99` reserved for DS use.
- **Applicability/presence:** `01-APP` and `02-BRW`; `01-PA` and `02-NPA`; `AReq = C`; condition: based on DS rules.
- **Privacy/security:** Expressly reports a signature-verification outcome and is therefore security-relevant. It is not itself stated to be a signature or encrypted value.
- **Citation:** [EMV 3DS v2.2.0, section A.4, p. 151, Table A.1; no local numbered Req]

### Physical PDF pages 152-153

#### 2.6 3DS Requestor Challenge Indicator - `threeDSRequestorChallengeInd`

- **Purpose and responsibility:** Indicates whether a challenge is requested. Examples cover Requestor concern for `01-PA`, adding a new wallet card for `02-NPA`, and local/regional mandates or other variables. Source: 3DS Server.
- **Type/format/values:** String, exactly 2 characters. `01` No preference; `02` No challenge requested; `03` Challenge requested (3DS Requestor preference); `04` Challenge requested (Mandate); `05` No challenge requested (transactional risk analysis is already performed); `06` No challenge requested (Data share only); `07` No challenge requested (strong consumer authentication is already performed); `08` No challenge requested (utilise whitelist exemption if no challenge required); `09` Challenge requested (whitelist prompt requested if challenge required); `10-79` reserved for EMVCo future use and invalid until defined; `80-99` reserved for DS use.
- **Default stated by source:** If absent, the ACS is expected to interpret it as `01` No preference.
- **Applicability/presence:** `01-APP` and `02-BRW`; `01-PA` and `02-NPA`; `AReq = O`; no additional conditional-inclusion text.
- **Privacy/security:** Material ACS decision input expressing preference, mandate, prior risk analysis, data-share-only purpose, prior strong authentication, or whitelist handling. The row does not say that the preference determines the ACS outcome by itself.
- **Citation:** [EMV 3DS v2.2.0, section A.4, pp. 152-153, Table A.1; no local numbered Req]

#### 2.7 3DS Requestor Decoupled Max Time - `threeDSRequestorDecMaxTime`

- **Purpose and responsibility:** Maximum time, in minutes, that the 3DS Requestor will wait for the ACS to provide the result of a Decoupled Authentication transaction. Source: 3DS Server; stated responding component: ACS.
- **Type/format/values:** String, stated length 5 characters; numeric values from `1` through `10080` accepted.
- **Applicability/presence:** `01-APP`, `02-BRW`, and `03-3RI`; `01-PA` and `02-NPA`; `AReq = O`; no additional conditional-inclusion text.
- **Privacy/security:** Availability/state-retention timing input for Decoupled Authentication; no confidentiality property is stated. The fixed-length/value-range tension is recorded in unresolved questions.
- **Citation:** [EMV 3DS v2.2.0, section A.4, p. 153, Table A.1; no local numbered Req]

### Physical PDF page 154

#### 2.8 3DS Requestor Decoupled Request Indicator - `threeDSRequestorDecReqInd`

- **Purpose and responsibility:** Indicates whether the Requestor asks the ACS to use Decoupled Authentication and agrees to its use if the ACS confirms. Source: 3DS Server; stated consumer/decision-maker: ACS.
- **Type/format/values:** String, exactly 1 character. `Y` = Decoupled Authentication is supported and preferred if a challenge is necessary; `N` = do not use Decoupled Authentication.
- **Default stated by source:** If absent, the ACS is expected to interpret it as `N`.
- **Applicability/presence:** `01-APP`, `02-BRW`, and `03-3RI`; `01-PA` and `02-NPA`; `AReq = O`; no additional conditional-inclusion text.
- **Privacy/security:** Material ACS flow-selection input. The row does not by itself authorise a `Y` confirmation; the paired ACS confirmation constraints appear under `acsDecConInd`.
- **Citation:** [EMV 3DS v2.2.0, section A.4, p. 154, Table A.1; no local numbered Req]

#### 2.9 3DS Requestor ID - `threeDSRequestorID`

- **Purpose and responsibility:** DS-assigned Requestor identifier; each DS supplies a unique ID to each Requestor individually. Source: 3DS Server; assigning authority: DS.
- **Type/format/values:** String, variable length, maximum 35 characters. A DS may impose its own formatting and character requirements.
- **Applicability/presence:** `01-APP`, `02-BRW`, and `03-3RI`; `01-PA` and `02-NPA`; `AReq = R`; no additional conditional-inclusion text.
- **Privacy/security:** Organisational/routing identifier; the row states no secrecy or authentication property.
- **Citation:** [EMV 3DS v2.2.0, section A.4, p. 154, Table A.1; no local numbered Req]

### Physical PDF page 155

#### 2.10 3DS Requestor Name - `threeDSRequestorName`

- **Purpose and responsibility:** DS-assigned Requestor name; each DS supplies a unique name to each Requestor individually. Source: 3DS Server; assigning authority: DS.
- **Type/format/values:** String, variable length, maximum 40 characters. A DS may impose its own formatting and character requirements.
- **Applicability/presence:** `01-APP`, `02-BRW`, and `03-3RI`; `01-PA` and `02-NPA`; `AReq = R`; no additional conditional-inclusion text.
- **Privacy/security:** Organisational identifier; the row states no secrecy or authentication property.
- **Citation:** [EMV 3DS v2.2.0, section A.4, p. 155, Table A.1; no local numbered Req]

#### 2.11 3DS Requestor Prior Transaction Authentication Information - `threeDSRequestorPriorAuthenticationInfo`

- **Purpose and responsibility:** Information about how the Requestor authenticated the Cardholder as part of a previous 3DS transaction. Source: 3DS Server.
- **Type/format/values:** Object, variable length. Refer to Table A.11 for included elements. The data is formatted as a JSON object before placement in this field.
- **Applicability/presence:** `01-APP`, `02-BRW`, and `03-3RI`; `01-PA` and `02-NPA`; `AReq = O`; Conditional Inclusion says "Optional, recommended to include."
- **Privacy/security:** Prior authentication history is security- and privacy-relevant. The row does not state field-level encryption, retention, or minimisation rules.
- **Citation:** [EMV 3DS v2.2.0, section A.4, p. 155, Table A.1; no local numbered Req]

### Physical PDF page 156

#### 2.12 3DS Requestor URL - `threeDSRequestorURL`

- **Purpose and responsibility:** Fully qualified URL of the Requestor website or customer-care site, supplying contact information to a receiving 3DS system if a problem arises. Source: 3DS Server.
- **Type/format/values:** String, variable length, maximum 2048 characters; fully qualified URL. Example: `http://server.domainname.com`.
- **Applicability/presence:** `01-APP`, `02-BRW`, and `03-3RI`; `01-PA` and `02-NPA`; `AReq = R`; no additional conditional-inclusion text.
- **Privacy/security:** Contact/routing metadata. The row's example is HTTP and does not itself state a transport-security rule; no HTTPS requirement is inferred from this row.
- **Citation:** [EMV 3DS v2.2.0, section A.4, p. 156, Table A.1; no local numbered Req]

#### 2.13 3DS Server Reference Number - `threeDSServerRefNumber`

- **Purpose and responsibility:** Unique identifier assigned by the EMVCo Secretariat after testing and approval. Source: 3DS Server; assigning authority: EMVCo Secretariat.
- **Type/format/values:** String, variable length, maximum 32 characters; value set by the EMVCo Secretariat.
- **Applicability/presence:** `01-APP`, `02-BRW`, and `03-3RI`; `01-PA` and `02-NPA`; `AReq = R`; `PReq = R`; no additional conditional-inclusion text.
- **Privacy/security:** Component/certification identity metadata; the row does not define it as a credential, secret, signature, or trust decision.
- **Citation:** [EMV 3DS v2.2.0, section A.4, p. 156, Table A.1; no local numbered Req]

#### 2.14 3DS Server Operator ID - `threeDSServerOperatorID`

- **Purpose and responsibility:** DS-assigned 3DS Server identifier; each DS may provide a unique ID to each Server individually. Source: 3DS Server; assigning authority: DS.
- **Type/format/values:** String, variable length, maximum 32 characters. A DS may impose its own formatting and character requirements.
- **Applicability/presence:** `01-APP`, `02-BRW`, and `03-3RI`; `01-PA` and `02-NPA`; `AReq = C`; `PReq = C`; presence requirements are DS-specific.
- **Privacy/security:** Operator identity/routing metadata; the row does not define it as an authentication credential.
- **Citation:** [EMV 3DS v2.2.0, section A.4, p. 156, Table A.1; no local numbered Req]

### Physical PDF page 157

#### 2.15 3DS Server Transaction ID - `threeDSServerTransID`

- **Purpose and responsibility:** Universally unique transaction identifier assigned by the 3DS Server to identify one transaction. Source: 3DS Server.
- **Type/format/values:** String, exactly 36 characters; canonical IETF RFC 4122 format. Any specified RFC 4122 version may be used if the output meets the stated requirements.
- **Applicability/presence:** `01-APP`, `02-BRW`, and `03-3RI`; `01-PA` and `02-NPA`; `AReq = R`, `ARes = R`, `CReq = R`, `CRes = R`, `PReq = R`, `PRes = R`, `RReq = R`, `RRes = R`, `Erro = C`.
- **Conditional inclusion:** Required in an Error Message if available, for example when obtainable from a message or being generated.
- **Privacy/security:** Correlation identifier used across protocol messages. The row does not call it a secret, authenticator, nonce, or encryption key.
- **Citation:** [EMV 3DS v2.2.0, section A.4, p. 157, Table A.1; no local numbered Req]

#### 2.16 3DS Server URL - `threeDSServerURL`

- **Purpose and responsibility:** Fully qualified URL to which the DS sends RReq after challenge completion. Incorrect formatting causes failure to deliver transaction results via RReq. Source: 3DS Server; stated sender/receiver for later use: DS to 3DS Server.
- **Type/format/values:** String, variable length, maximum 2048 characters; fully qualified URL. Example: `https://server.adomainname.net`.
- **Applicability/presence:** `01-APP`, `02-BRW`, and `03-3RI`; `01-PA` and `02-NPA`; `AReq = R`; no additional conditional-inclusion text.
- **Privacy/security:** Result-routing endpoint. The row does not state that the URL value is secret or field-level encrypted.
- **Citation:** [EMV 3DS v2.2.0, section A.4, p. 157, Table A.1; no local numbered Req]

### Physical PDF page 158

#### 2.17 3RI Indicator - `threeRIInd`

- **Purpose and responsibility:** Identifies the type of 3RI request and gives the ACS information to determine the best handling approach. Source: 3DS Server; stated consumer: ACS.
- **Type/format/values:** String, exactly 2 characters. `01` Recurring transaction; `02` Instalment transaction; `03` Add card; `04` Maintain card information; `05` Account verification; `06` Split/delayed shipment; `07` Top-up; `08` Mail Order; `09` Telephone Order; `10` Whitelist status check; `11` Other payment; `12-79` reserved for EMVCo future use and invalid until defined; `80-99` reserved for DS use.
- **Applicability/presence:** `03-3RI`; `01-PA` and `02-NPA`; `AReq = R`; no additional conditional-inclusion text.
- **Privacy/security:** Material ACS decision input describing the 3RI use case. It does not itself prove authentication, authorisation, or scheme classification beyond the listed value.
- **Citation:** [EMV 3DS v2.2.0, section A.4, p. 158, Table A.1; no local numbered Req]

### Physical PDF page 159

#### 2.18 Account Type - `acctType`

- **Purpose and responsibility:** Indicates account type, for example for a multi-account card product. Source: 3DS Server.
- **Type/format/values:** String, exactly 2 characters. `01` Not Applicable; `02` Credit; `03` Debit; `04-79` reserved for EMVCo future use and invalid until defined; `80-99` DS- or Payment System-specific.
- **Applicability/presence:** `01-APP`, `02-BRW`, and `03-3RI`; `01-PA` and `02-NPA`; `AReq = C`.
- **Conditional inclusion:** Required if the Requestor asks the Cardholder which account type is being used before purchase; required in some markets, with Brazil merchants given as an example; otherwise optional.
- **Privacy/security:** Card/account classification data relevant to transaction processing; the row states no field-level protection or policy.
- **Citation:** [EMV 3DS v2.2.0, section A.4, p. 159, Table A.1; no local numbered Req]

#### 2.19 Acquirer BIN - `acquirerBIN`

- **Purpose and responsibility:** Acquiring-institution identification code assigned by the DS receiving AReq. Source: 3DS Server; assigning/defining authority: each Payment System or DS.
- **Type/format/values:** String, variable length, maximum 11 characters. The value correlates to the Acquirer BIN as defined by each Payment System or DS.
- **Applicability/presence:** `01-APP`, `02-BRW`, and `03-3RI`; `01-PA`: `AReq = R`; `02-NPA`: `AReq = O`; no additional conditional-inclusion text.
- **Privacy/security:** Institution/routing identifier; the row does not define it as secret or as proof of acquirer identity.
- **Citation:** [EMV 3DS v2.2.0, section A.4, p. 159, Table A.1; no local numbered Req]

### Physical PDF page 160

#### 2.20 Acquirer Merchant ID - `acquirerMerchantID`

- **Purpose and responsibility:** Acquirer-assigned Merchant identifier. It may equal the value used in authorisation requests on behalf of the Requestor and is represented in ISO 8583 formatting requirements. Source: 3DS Server.
- **Type/format/values:** String, variable length, maximum 35 characters. Individual Directory Servers may impose format and character requirements.
- **Applicability/presence:** `01-APP`, `02-BRW`, and `03-3RI`; `01-PA`: `AReq = R`; `02-NPA`: `AReq = O`; no additional conditional-inclusion text.
- **Privacy/security:** Merchant/account-routing identity metadata; the row states no secrecy or authentication property.
- **Citation:** [EMV 3DS v2.2.0, section A.4, p. 160, Table A.1; no local numbered Req]

#### 2.21 ACS Challenge Mandated Indicator - `acsChallengeMandated`

- **Purpose and responsibility:** Indicates whether a challenge is required for transaction authorisation due to local/regional mandates or another variable. Source: ACS.
- **Type/format/values:** String, exactly 1 character. `Y` = challenge is mandated; `N` = challenge is not mandated.
- **Applicability/presence:** `01-APP`, `02-BRW`, and `03-3RI`; `01-PA` and `02-NPA`; `ARes = C`; required if Transaction Status is `C` or `D`.
- **Privacy/security:** Express ACS output affecting downstream handling of challenge/Decoupled outcomes; the row states no cryptographic property.
- **Citation:** [EMV 3DS v2.2.0, section A.4, p. 160, Table A.1; no local numbered Req]

#### 2.22 ACS Counter ACS to SDK - `acsCounterAtoS`

- **Purpose and responsibility:** Counter used as a security measure in the ACS-to-SDK secure channel. Source: ACS.
- **Type/format/values:** String, exactly 3 characters; no allowed-value list is provided in this row.
- **Applicability/presence:** `01-APP`; `01-PA` and `02-NPA`; `CRes = R`; no additional conditional-inclusion text.
- **Privacy/security:** Explicit security measure for the direct App secure channel. This row does not define counter arithmetic or encryption; those details are outside this table row.
- **Citation:** [EMV 3DS v2.2.0, section A.4, p. 160, Table A.1; no local numbered Req]

### Physical PDF page 161

#### 2.23 ACS Decoupled Confirmation Indicator - `acsDecConInd`

- **Purpose and responsibility:** Indicates whether the ACS confirms and agrees to use Decoupled Authentication to authenticate the Cardholder. Source: ACS.
- **Type/format/values:** String, exactly 1 character. `Y` = confirms Decoupled Authentication will be used; `N` = Decoupled Authentication will not be used.
- **Value constraints stated by source:** If `threeDSRequestorDecReqInd = N`, the ACS cannot return `Y`. If Transaction Status is `D`, `N` is invalid.
- **Applicability/presence:** `01-APP`, `02-BRW`, and `03-3RI`; `01-PA` and `02-NPA`; `ARes = C`; required if Transaction Status is `D`.
- **Privacy/security:** Material ACS flow-selection output and consistency rule; the row states no confidentiality or cryptographic property.
- **Citation:** [EMV 3DS v2.2.0, section A.4, p. 161, Table A.1; no local numbered Req]

### Physical PDF page 162

#### 2.24 ACS Ephemeral Public Key (QT) - `acsEphemPubKey`

- **Purpose and responsibility:** Public-key component of the ACS-generated ephemeral key pair used to establish session keys between SDK and ACS. It is inside the ACS Signed Content JWS object. The row cross-references section 6.2.3.2. Source: ACS.
- **Type/format/values:** Object, variable length, maximum 256 characters; no separate allowed-value list in this row.
- **Applicability/presence:** `01-APP`; `01-PA` and `02-NPA`; Message Inclusion says "See ACS Signed Content"; Conditional Inclusion says "See ACS Signed Content."
- **Privacy/security:** Express secure-channel public-key material. It is public-key data, not a secret session key. The Object/character-limit edit is unresolved rather than normalised.
- **Citation:** [EMV 3DS v2.2.0, section A.4, p. 162, Table A.1; no local numbered Req]

#### 2.25 ACS HTML - `acsHTML`

- **Purpose and responsibility:** HTML provided by the ACS in CRes when HTML is selected as the ACS UI Type for the Cardholder challenge. Source: ACS.
- **Type/format/values:** String, variable length, maximum 100KB. Accepted value: Base64url-encoded HTML; the value is Base64url encoded before placement in CRes.
- **Applicability/presence:** `01-APP`; `01-PA` and `02-NPA`; `CRes = C`; required when the ACS selects ACS UI Type `5` (HTML), as printed in the conditional cell.
- **Privacy/security:** Challenge UI content. Base64url is encoding, not encryption; no confidentiality property is inferred. The 100KB measurement basis is unresolved.
- **Citation:** [EMV 3DS v2.2.0, section A.4, p. 162, Table A.1; no local numbered Req]

### Physical PDF page 163

#### 2.26 ACS Operator ID - `acsOperatorID`

- **Purpose and responsibility:** DS-assigned ACS identifier; each DS may give each ACS a unique ID. Source: ACS; assigning authority: DS.
- **Type/format/values:** String, variable length, maximum 32 characters. A DS may impose its own formatting and character requirements.
- **Applicability/presence:** `01-APP`, `02-BRW`, and `03-3RI`; `01-PA` and `02-NPA`; `ARes = C`; presence requirements are DS-specific.
- **Privacy/security:** Component/operator identity metadata; the row does not define it as a credential or cryptographic proof.
- **Citation:** [EMV 3DS v2.2.0, section A.4, p. 163, Table A.1; no local numbered Req]

#### 2.27 ACS Reference Number - `acsReferenceNumber`

- **Purpose and responsibility:** Unique identifier assigned by the EMVCo Secretariat after testing and approval. Source: ACS; assigning authority: EMVCo Secretariat.
- **Type/format/values:** String, variable length, maximum 32 characters; value set by the EMVCo Secretariat.
- **Applicability/presence:** `01-APP`, `02-BRW`, and `03-3RI`; `01-PA` and `02-NPA`; `ARes = R`; no additional conditional-inclusion text.
- **Privacy/security:** Component/certification identity metadata; the row does not define it as a secret, credential, signature, or trust decision.
- **Citation:** [EMV 3DS v2.2.0, section A.4, p. 163, Table A.1; no local numbered Req]

#### 2.28 ACS Rendering Type - `acsRenderingType`

- **Purpose and responsibility:** Identifies the ACS UI Template that the ACS first presents to the consumer. Source: ACS.
- **Type/format/values:** Object; no length is stated. Refer to Table A.12 for included elements. Data is formatted into a JSON object before placement in this field.
- **Applicability/presence:** `01-APP`; `01-PA` and `02-NPA`; `ARes = C`; `RReq = C`.
- **Conditional inclusion:** For ARes, required if Transaction Status is `C`. For RReq, required unless ACS Decoupled Confirmation is `Y`.
- **Privacy/security:** Material ACS UI/rendering output. The row states no confidentiality or cryptographic property.
- **Citation:** [EMV 3DS v2.2.0, section A.4, p. 163, Table A.1; no local numbered Req]

### Physical PDF page 164

#### 2.29 ACS Signed Content - `acsSignedContent`

- **Purpose and responsibility:** JWS object, represented as a string, created by the ACS for ARes. The row cross-references section 6.2.3.2. Source: ACS.
- **Type/format/values:** String, variable length. The JWS body contains ACS URL, ACS Ephemeral Public Key (`QT`), and SDK Ephemeral Public Key (`QC`) as defined in Table A.1.
- **Applicability/presence:** `01-APP`; `01-PA` and `02-NPA`; `ARes = C`; required if Transaction Status is `C`.
- **Privacy/security:** Signed secure-channel setup content. JWS supplies signed content, not encryption; this table row does not claim confidentiality.
- **Citation:** [EMV 3DS v2.2.0, section A.4, p. 164, Table A.1; no local numbered Req]

#### 2.30 ACS Transaction ID - `acsTransID`

- **Purpose and responsibility:** Universally unique transaction identifier assigned by the ACS to identify one transaction. Source: ACS.
- **Type/format/values:** String, exactly 36 characters; canonical IETF RFC 4122 format. Any specified RFC 4122 version may be used if the output meets the stated requirements.
- **Applicability/presence:** `01-APP`, `02-BRW`, and `03-3RI`; `01-PA` and `02-NPA`; `ARes = R`, `CReq = R`, `CRes = R`, `RReq = R`, `RRes = R`, `Erro = C`.
- **Conditional inclusion:** Required in an Error Message if available, for example when obtainable from a message or being generated.
- **Privacy/security:** ACS correlation identifier. The row does not call it a secret, authenticator, nonce, or encryption key.
- **Citation:** [EMV 3DS v2.2.0, section A.4, p. 164, Table A.1; no local numbered Req]

### Physical PDF page 165

#### 2.31 ACS UI Type - `acsUiType`

- **Purpose and responsibility:** Identifies the UI type that the SDK renders, including its data mapping and requirements. Source: ACS; stated renderer: 3DS SDK.
- **Type/format/values:** String, exactly 2 characters. `01` Text; `02` Single Select; `03` Multi Select; `04` OOB; `05` HTML; `06-79` reserved for EMVCo future use and invalid until defined; `80-99` reserved for DS use.
- **Applicability/presence:** `01-APP`; `01-PA` and `02-NPA`; `CRes = R`; no additional conditional-inclusion text.
- **Privacy/security:** Material SDK rendering instruction. It does not state that any UI payload is encrypted; protection follows the applicable CRes channel rules outside this row.
- **Citation:** [EMV 3DS v2.2.0, section A.4, p. 165, Table A.1; no local numbered Req]

### Physical PDF page 166

#### 2.32 ACS URL - `acsURL`

- **Purpose and responsibility:** Fully qualified ACS URL used for challenge. In `01-APP`, the SDK sends CReq to it; in `02-BRW`, the Requestor posts CReq to it through the challenge window. For App it is inside the ACS Signed Content JWS object; for Browser it is present as its own object. Source: ACS.
- **Type/format/values:** String, variable length, maximum 2048 characters; fully qualified URL. Example: `https://server.acsdomainname.com`.
- **Applicability/presence:** `01-APP` and `02-BRW`; `01-PA` and `02-NPA`. For `01-APP`, Message Inclusion and Conditional Inclusion both say to see ACS Signed Content. For `02-BRW`, `ARes = C` and the field is required if Transaction Status is `C`.
- **Privacy/security:** Challenge-routing endpoint. For App the row places it in ACS Signed Content, but the URL is not encrypted merely by being in JWS; for Browser it is its own ARes field.
- **Citation:** [EMV 3DS v2.2.0, section A.4, p. 166, Table A.1; no local numbered Req]

#### 2.33 Address Match Indicator - `addrMatch`

- **Purpose and responsibility:** Indicates whether the Cardholder Shipping Address and Cardholder Billing Address are the same. Source: 3DS Server.
- **Type/format/values:** String, exactly 1 character. `Y` = Shipping Address matches Billing Address; `N` = Shipping Address does not match Billing Address.
- **Applicability/presence:** `01-APP` and `02-BRW`; `01-PA` and `02-NPA`; `AReq = O`; no additional conditional-inclusion text.
- **Privacy/security:** Derived address-comparison information and therefore privacy-relevant. It does not carry either address itself, and the row states no field-level encryption.
- **Citation:** [EMV 3DS v2.2.0, section A.4, p. 166, Table A.1; no local numbered Req]

## 3. ACS behavior traceability

| ACS behavior area | Material Part 1 elements | Source-prescribed effect | Evidence |
|---|---|---|---|
| AReq validation | All `R` and applicable `C` fields | Missing required/conditionally required fields lead to Error Code `201`; a present field failing a listed edit leads to `203`; unlisted validations are not to be used for rejection. | [EMV 3DS v2.2.0, sections A.1-A.2, p. 147; no local numbered Req] |
| Browser Method context | `threeDSCompInd` | ACS receives a required Browser AReq signal distinguishing Method completion, failure, and unavailability caused by absence of a Method URL in PRes. | [EMV 3DS v2.2.0, section A.4, p. 148, Table A.1; no local numbered Req] |
| Authentication and challenge decision inputs | `threeDSRequestorAuthenticationInd`, `threeDSRequestorAuthenticationInfo`, `threeDSReqAuthMethodInd`, `threeDSRequestorChallengeInd`, `threeRIInd`, `acctType` | Values communicate request type, prior/current Requestor authentication, DS verification, challenge preference/mandate context, 3RI use case, and account type. The challenge indicator defaults to `01` when absent. | [EMV 3DS v2.2.0, section A.4, pp. 150-153 and 158-159, Table A.1; no local numbered Req] |
| Decoupled selection and timing | `threeDSRequestorDecMaxTime`, `threeDSRequestorDecReqInd`, `acsDecConInd` | The Requestor states its wait time and preference; absent request indicator is interpreted as `N`; ACS cannot confirm `Y` after Requestor `N`; Transaction Status `D` makes `N` invalid and makes the confirmation field required. | [EMV 3DS v2.2.0, section A.4, pp. 153-154 and 161, Table A.1; no local numbered Req] |
| Challenge/result routing and correlation | `threeDSServerTransID`, `threeDSServerURL`, `acsTransID`, `acsURL` | IDs correlate the transaction across their listed messages; Server URL routes RReq; ACS URL routes App/Browser CReq; available IDs are conditionally required in Error Messages. | [EMV 3DS v2.2.0, section A.4, pp. 157, 164, and 166, Table A.1; no local numbered Req] |
| App secure-channel setup | `acsEphemPubKey`, `acsSignedContent` | ACS Signed Content is conditionally required for App status `C` and contains ACS URL plus ACS and SDK ephemeral public keys; the ACS key row points back to that signed content. | [EMV 3DS v2.2.0, section A.4, pp. 162 and 164, Table A.1; no local numbered Req] |
| App challenge UI and channel sequencing | `acsRenderingType`, `acsUiType`, `acsHTML`, `acsCounterAtoS` | ACS selects/rendering fields as listed; HTML is conditional on HTML UI type; ACS-to-SDK counter is required in CRes and expressly described as a security measure. Base64url HTML remains encoding only. | [EMV 3DS v2.2.0, section A.4, pp. 160 and 162-165, Table A.1; no local numbered Req] |
| ACS identity and mandate output | `acsChallengeMandated`, `acsOperatorID`, `acsReferenceNumber` | ACS reports challenge mandate when status is `C` or `D`; operator ID presence is DS-specific; the EMVCo-assigned reference number is required in ARes. | [EMV 3DS v2.2.0, section A.4, pp. 160 and 163, Table A.1; no local numbered Req] |

## 4. Unresolved items in this batch

- `threeDSRequestorDecMaxTime` is declared as exactly 5 characters while accepted numeric values range from `1` through `10080`; Table A.1 does not say whether shorter values must be zero-padded. Do not choose a padding rule without authoritative clarification. [EMV 3DS v2.2.0, section A.4, p. 153, Table A.1]
- `acsEphemPubKey` is a JSON Object with a variable maximum of 256 characters. Table A.1 does not state how the character limit is measured for the object representation. Preserve the printed constraint and confirm the applicable object/JWK profile before implementing the edit. [EMV 3DS v2.2.0, section A.4, p. 162, Table A.1]
- `acsHTML` has a maximum length of 100KB, but the row does not define decimal versus binary KB or whether the limit applies before or after Base64url encoding. Do not infer a measurement basis. [EMV 3DS v2.2.0, section A.4, p. 162, Table A.1]

## 5. Verification record

- All physical PDF pages 145-166 were rendered and directly inspected, including page 146 inclusion definitions and the multi-column Table A.1 cells on pages 148-166.
- The row sequence was compared against the extracted text and the PDF page images. Count: 33 complete rows.
- First row comparison: page 148 begins the scoped register with **3DS Method Completion Indicator** / `threeDSCompInd`.
- Last row comparison: page 166 ends the scoped register with **Address Match Indicator** / `addrMatch`; **ACS URL** / `acsURL` immediately precedes it on the same page.
- Boundary comparison: physical page 167 begins **Authentication Method**, which was not captured.
- No local numbered requirements appear in Annex A.1-A.4 on physical pages 145-166; each entry therefore cites section, physical page, and Table A.1 without inventing a Req number.

# EMV 3DS v2.2.0 Detailed Values and Message Extensions

## 1. Scope, source, and cross-links

This batch covers Annex A.5, **Detailed Field Values**, and Annex A.6, **Message Extension Data**, on physical PDF pages 223-237. It captures Tables A.2-A.7, their surrounding rules and examples, and a searchable ACS-focused index of related Table A.1 values already analysed in the four data-element files. Physical page 237 also starts Annex A.7; only that boundary is recorded here, and no A.7 content is analysed. [EMV 3DS v2.2.0, sections A.5-A.6.3, pp. 223-237, Tables A.2-A.7; no local numbered Reqs]

The PDF in `source/` is the normative source. The extracted text was used for navigation, and every physical page from 223 through 237 was rendered and inspected directly. The four linked Table A.1 registers remain the element-level source for message presence, channel/category applicability, and conditions:

- [Data Elements Part 1](./10-data-elements-part-1.md) - pages 148-166.
- [Data Elements Part 2](./11-data-elements-part-2.md) - pages 167-185.
- [Data Elements Part 3](./12-data-elements-part-3.md) - pages 186-204.
- [Data Elements Part 4](./13-data-elements-part-4.md) - pages 205-222.

No locally numbered requirements are printed in Annex A.5 or A.6. Normative claims in this file therefore cite the section, physical PDF page, and table; numbered requirements are included only when a cross-reference outside the scoped pages is material.

## 2. Annex A.5 - Detailed Field Values

### 2.1 Device Information - `01-APP` only

The 3DS SDK gathers Device Information under the separate EMV 3-D Secure SDK - Device Information specification and places it in a JWE object encrypted with the DS public key. This section adds no value table. The associated Table A.1 fields are [`deviceInfo`](./12-data-elements-part-3.md) and [`sdkEncData`](./13-data-elements-part-4.md); the former is the DS-decrypted ACS-bound value and the latter is the SDK-encrypted Server-to-DS value. [EMV 3DS v2.2.0, section A.5.1, p. 223; section A.3, p. 147; section A.4, pp. 196 and 212, Table A.1; no local numbered Req]

### 2.2 Browser Information - `02-BRW` only

Accurate Browser Information is obtained in AReq so the ACS can determine whether it can support authentication on the particular Cardholder browser. The 3DS Server is responsible for accurately populating the data for every transaction, ensuring it is not altered or hard-coded, and ensuring it is unique to each transaction. Section A.5.2 lists the following fields, all linked to [Data Elements Part 2](./11-data-elements-part-2.md):

| Listed Browser field | Table A.1 field | AReq presence |
|---|---|---|
| Browser Accept Headers | `browserAcceptHeader` | Required |
| Browser IP Address | `browserIP` | Conditional - shall be included where regionally acceptable |
| Browser Java Enabled | `browserJavaEnabled` | Conditional - required when `browserJavascriptEnabled = true`, otherwise optional |
| Browser Language | `browserLanguage` | Required |
| Browser Screen Color Depth | `browserColorDepth` | Conditional - required when `browserJavascriptEnabled = true`, otherwise optional |
| Browser Screen Height | `browserScreenHeight` | Conditional - required when `browserJavascriptEnabled = true`, otherwise optional |
| Browser Screen Width | `browserScreenWidth` | Conditional - required when `browserJavascriptEnabled = true`, otherwise optional |
| Browser Time Zone | `browserTZ` | Conditional - required when `browserJavascriptEnabled = true`, otherwise optional |
| Browser User-Agent | `browserUserAgent` | Required |

The list does not include the required Table A.1 field `browserJavascriptEnabled`, even though that row refers back to A.5.2. The omission and the phrase “unique to each transaction” remain unresolved; browser values that naturally repeat must not be changed or fabricated merely to make them different. [EMV 3DS v2.2.0, section A.5.2, p. 223; section A.4, pp. 170-175, Table A.1; no local numbered Req]

### 2.3 Table A.2 - 3DS Method Data

The HTTP form field name is `threeDSMethodData`. The data is exchanged between the 3DS Requestor and ACS through the Cardholder browser. Table A.2 does not print length or JSON-type edits for its two child fields. [EMV 3DS v2.2.0, section A.5.3, p. 224, Table A.2; no local numbered Req]

| Data element / field | Direction and recipient | Category | Inclusion | Exact role and linkage | Citation |
|---|---|---|---|---|---|
| 3DS Method Notification URL / `threeDSMethodNotificationURL` | Initial 3DS Requestor browser POST -> ACS | `01-PA`, `02-NPA` | `R` | URL that receives the ACS notification of 3DS Method completion. This field appears in the initial request only. | [EMV 3DS v2.2.0, section A.5.3, p. 224, Table A.2; no local numbered Req] |
| 3DS Server Transaction ID / `threeDSServerTransID` | Initial Requestor browser POST -> ACS; ACS notification POST -> 3DS Requestor | `01-PA`, `02-NPA` | `R` | Same transaction identifier and format as [`threeDSServerTransID`](./10-data-elements-part-1.md) in AReq; returned in the POST to the notification URL. | [EMV 3DS v2.2.0, section A.5.3, p. 224, Table A.2; section A.4, p. 157, Table A.1; no local numbered Req] |

The page-225 examples show the initial `threeDSMethodData` value decoding to both child fields and the ACS completion value decoding to `threeDSServerTransID` only. The examples use the same UUID, `3ac7caa7-aa42-2663-791b-2ac05a542c4a`, in both directions. They illustrate the envelope and correlation; they do not create a third protocol message type. [EMV 3DS v2.2.0, section A.5.3, p. 225; section 5.8.1, pp. 122-123, Reqs 259-263]

### 2.4 Table A.3 - Browser CReq/CRes POST Data

A form in the Cardholder browser carries the Browser CReq flow to the ACS and the final CRes flow to the [`notificationURL`](./13-data-elements-part-4.md) in an HTTP POST. These HTTP fields are transport-envelope fields, not additional EMV 3DS protocol messages. [EMV 3DS v2.2.0, section A.5.4, p. 226, Table A.3; no local numbered Req]

| Data element / field | Recipient(s) and use | Length / format | Inclusion | Exact handling rule | Citation |
|---|---|---|---|---|---|
| 3DS Requestor Session Data / `threeDSSessionData` | ACS on the CReq POST and 3DS Requestor on the returned CRes POST | Maximum `1024`; printed format “Alphanumeric”; Base64url encoded; size after Base64url encoding, if applicable, is limited to `1024` bytes | `O` | May be absent when the Requestor can correlate the final POST without help. May carry any Requestor data needed to continue a session. ACS preserves it unchanged and without assumptions. | [EMV 3DS v2.2.0, section A.5.4, p. 226, Table A.3; no local numbered Req] |
| CReq / `creq` | ACS | Variable length; Base64url | `R` | Entire CReq defined in Table B.3, Base64url encoded. | [EMV 3DS v2.2.0, section A.5.4, p. 226, Table A.3; no local numbered Req] |
| CRes / `cres` | 3DS Requestor | The row prints no separate length value; its description says the entire CRes is Base64url encoded | `R` | Table A.3 points to Table B.4. Req 138 instead points the final Browser CRes to Table B.5; that source conflict remains unresolved. | [EMV 3DS v2.2.0, section A.5.4, p. 226, Table A.3; section 3.3, p. 70, Req 138; sections B.4-B.5, pp. 265-266] |

### 2.5 Table A.4 - Error Code, Error Description, and Error Detail

Table A.4 defines how to formulate an Error Message when a received message cannot be processed or when error handling is required after receiving a response. Its Error Description and Error Detail text is guidance on expected content; where Error Detail calls for required elements, those elements are expected to be listed appropriately. The associated Table A.1 fields are [`errorCode`, `errorDescription`, `errorDetail`, `errorComponent`, and `errorMessageType`](./12-data-elements-part-3.md). [EMV 3DS v2.2.0, section A.5.5, p. 227, Table A.4; section A.4, p. 199, Table A.1; no local numbered Req]

| Code | Error Code meaning | Error Description condition | Expected Error Detail | Citation |
|---:|---|---|---|---|
| `101` | Message Received Invalid | Message is not `AReq`, `ARes`, `CReq`, `CRes`, `PReq`, `PRes`, `RReq`, or `RRes`; a valid Message Type is sent to or from an inappropriate component; or the message is not recognised. | One of: `Invalid Message Type`; `Invalid Message for the receiving component`; `Invalid Formatted Message`. | [EMV 3DS v2.2.0, section A.5.5, p. 227, Table A.4; no local numbered Req] |
| `102` | Message Version Number Not Supported | Received Message Version Number is not valid for the receiving component. | All supported Protocol Version Numbers in a comma-delimited list. | [EMV 3DS v2.2.0, section A.5.5, p. 227, Table A.4; no local numbered Req] |
| `103` | Sent Messages Limit Exceeded | Maximum number of PReq messages sent to the DS was exceeded. | Example: the 3DS Server sends two PReq messages to the DS within one hour. | [EMV 3DS v2.2.0, section A.5.5, p. 227, Table A.4; no local numbered Req] |
| `201` | Required Data Element Missing | A message element required by Table A.1 is missing. | Name of omitted required element(s), comma-delimited when more than one. Parent example: `messageType`; parent/child example: `acctInfo.chAccAgeInd`. | [EMV 3DS v2.2.0, section A.5.5, p. 228, Table A.4; no local numbered Req] |
| `202` | Critical Message Extension Not Recognised | Critical message extension not recognised. | ID of unrecognised critical Message Extension(s), comma-delimited when more than one. | [EMV 3DS v2.2.0, section A.5.5, p. 228, Table A.4; no local numbered Req] |
| `203` | Format of one or more Data Elements is Invalid according to the Specification | A data element has an invalid Table A.1 format/value; Message Version Number differs from AReq; or a data element is present where its conditional inclusion does not apply. | Name of invalid element(s), comma-delimited when more than one. | [EMV 3DS v2.2.0, section A.5.5, p. 228, Table A.4; no local numbered Req] |
| `204` | Duplicate Data Element | A valid data element is present more than once. | Name of duplicated element(s), comma-delimited when more than one. | [EMV 3DS v2.2.0, section A.5.5, p. 228, Table A.4; no local numbered Req] |
| `301` | Transaction ID Not Recognised | Transaction ID is not valid for the receiving component. | Transaction ID received was invalid; “invalid” includes not recognised or recognised as a duplicate. | [EMV 3DS v2.2.0, section A.5.5, p. 228, Table A.4; no local numbered Req] |
| `302` | Data Decryption Failure | Receiving system could not decrypt data for a technical or other reason. | Description of the failure. | [EMV 3DS v2.2.0, section A.5.5, p. 229, Table A.4; no local numbered Req] |
| `303` | Access Denied, Invalid Endpoint | Access denied or endpoint invalid. | Description of the failure. | [EMV 3DS v2.2.0, section A.5.5, p. 229, Table A.4; no local numbered Req] |
| `304` | ISO Code Invalid | Country or currency code is invalid under the ISO tables, or is excluded by Table A.5. | Name of invalid element(s), comma-delimited when more than one. The source then says that if `Challenge Request.Purchase.currency` and `Challenge Request.Purchase.exponent` form an invalid pair, list both “as Error Description”; that wording is preserved as an unresolved source issue. | [EMV 3DS v2.2.0, section A.5.5, p. 229, Table A.4; no local numbered Req] |
| `305` | Transaction Data Not Valid | For AReq: Cardholder Account Number is outside an Issuer range. For CReq sent incorrectly: wrong ACS; CReq not expected from ARes values; or the same ACS Transaction ID CReq was already received and processed. | Name of element(s) causing the ACS to decide AReq/CReq was incorrectly sent, comma-delimited when more than one. | [EMV 3DS v2.2.0, section A.5.5, p. 229, Table A.4; no local numbered Req] |
| `306` | Merchant Category Code (MCC) Not Valid for Payment System | MCC is not valid for the Payment System. | Example: invalid MCC received in AReq. | [EMV 3DS v2.2.0, section A.5.5, p. 229, Table A.4; no local numbered Req] |
| `307` | Serial Number Not Valid | Serial Number is invalid. | Example: invalid Serial Number in PReq/PRes, such as too old or not found. | [EMV 3DS v2.2.0, section A.5.5, p. 229, Table A.4; no local numbered Req] |
| `402` | Transaction Timed Out | Transaction timed out. | Example: timeout expiry reached under section 5.5. | [EMV 3DS v2.2.0, section A.5.5, p. 230, Table A.4; no local numbered Req] |
| `403` | Transient System Failure | Transient system failure. | Example: a slowly processing back-end system. | [EMV 3DS v2.2.0, section A.5.5, p. 230, Table A.4; no local numbered Req] |
| `404` | Permanent System Failure | Permanent system failure. | Example: a critical database cannot be accessed. | [EMV 3DS v2.2.0, section A.5.5, p. 230, Table A.4; no local numbered Req] |
| `405` | System Connection Failure | System connection failure. | Example: sending component cannot establish a connection to the receiving component. | [EMV 3DS v2.2.0, section A.5.5, p. 230, Table A.4; no local numbered Req] |

Table A.4 states no reserved, future-use, or deprecated Error Code ranges. The listed codes must remain distinct: for example, `201` missing, `203` invalid format/value/condition, `204` duplicate element, `301` invalid or duplicate Transaction ID, and `305` invalid transaction state are not interchangeable. [EMV 3DS v2.2.0, section A.5.5, pp. 227-230, Table A.4; no local numbered Req]

### 2.6 Table A.5 - Excluded ISO Currency and Country Code Values

The numeric tokens are typed by their ISO code set. An ISO 4217 value and an ISO 3166-1 value must not be merged merely because their numeric ranges overlap. Error Code `304` applies to an invalid ISO code or a value excluded by this table. [EMV 3DS v2.2.0, sections A.5.5-A.5.6, pp. 229 and 231, Tables A.4-A.5; no local numbered Req]

| ISO code set | Value not permitted for 3-D Secure | Exact definition / status | Relevant Table A.1 elements | Citation |
|---|---:|---|---|---|
| ISO 4217 | `955` | European Composite Unit | [`purchaseCurrency`](./13-data-elements-part-4.md) and other ISO 4217 fields | [EMV 3DS v2.2.0, section A.5.6, p. 231, Table A.5; no local numbered Req] |
| ISO 4217 | `956` | European Monetary Unit | [`purchaseCurrency`](./13-data-elements-part-4.md) and other ISO 4217 fields | [EMV 3DS v2.2.0, section A.5.6, p. 231, Table A.5; no local numbered Req] |
| ISO 4217 | `957` | European Unit of Account 9 | [`purchaseCurrency`](./13-data-elements-part-4.md) and other ISO 4217 fields | [EMV 3DS v2.2.0, section A.5.6, p. 231, Table A.5; no local numbered Req] |
| ISO 4217 | `958` | European Unit of Account 17 | [`purchaseCurrency`](./13-data-elements-part-4.md) and other ISO 4217 fields | [EMV 3DS v2.2.0, section A.5.6, p. 231, Table A.5; no local numbered Req] |
| ISO 4217 | `959` | Gold | [`purchaseCurrency`](./13-data-elements-part-4.md) and other ISO 4217 fields | [EMV 3DS v2.2.0, section A.5.6, p. 231, Table A.5; no local numbered Req] |
| ISO 4217 | `960` | I.M.F. | [`purchaseCurrency`](./13-data-elements-part-4.md) and other ISO 4217 fields | [EMV 3DS v2.2.0, section A.5.6, p. 231, Table A.5; no local numbered Req] |
| ISO 4217 | `961` | Silver | [`purchaseCurrency`](./13-data-elements-part-4.md) and other ISO 4217 fields | [EMV 3DS v2.2.0, section A.5.6, p. 231, Table A.5; no local numbered Req] |
| ISO 4217 | `962` | Platinum | [`purchaseCurrency`](./13-data-elements-part-4.md) and other ISO 4217 fields | [EMV 3DS v2.2.0, section A.5.6, p. 231, Table A.5; no local numbered Req] |
| ISO 4217 | `963` | Reserved for testing | [`purchaseCurrency`](./13-data-elements-part-4.md) and other ISO 4217 fields | [EMV 3DS v2.2.0, section A.5.6, p. 231, Table A.5; no local numbered Req] |
| ISO 4217 | `964` | Palladium | [`purchaseCurrency`](./13-data-elements-part-4.md) and other ISO 4217 fields | [EMV 3DS v2.2.0, section A.5.6, p. 231, Table A.5; no local numbered Req] |
| ISO 4217 | `999` | No currency is involved | [`purchaseCurrency`](./13-data-elements-part-4.md) and other ISO 4217 fields | [EMV 3DS v2.2.0, section A.5.6, p. 231, Table A.5; no local numbered Req] |
| ISO 3166-1 | `901-999` | Reserved by ISO to designate country names not otherwise defined | [`billAddrCountry`, `shipAddrCountry`](./11-data-elements-part-2.md), [`merchantCountryCode`](./12-data-elements-part-3.md) | [EMV 3DS v2.2.0, section A.5.6, p. 231, Table A.5; no local numbered Req] |

### 2.7 Table A.6 - Card Range Data

[`cardRangeData`](./11-data-elements-part-2.md) is returned from the DS to the 3DS Server in PRes. It identifies the most recent EMV 3DS version supported by the ACS hosting the range and may include the ACS 3DS Method URL and DS Start/End Protocol Versions. There may be as many JSON objects as stored card ranges in the called DS. [EMV 3DS v2.2.0, section A.5.7, p. 232, Table A.6; section A.4, p. 175, Table A.1; no local numbered Req]

| Data element / field | Exact meaning | Length / type / value | Inclusion | Citation |
|---|---|---|---|---|
| 3DS Method URL / `threeDSMethodURL` | ACS URL used by the 3DS Method; may be omitted when the ACS does not support it for the specific card range | Variable, maximum 256 characters; String; fully qualified URL | `O` | [EMV 3DS v2.2.0, section A.5.7, p. 232, Table A.6; no local numbered Req] |
| ACS End Protocol Version / `acsEndProtocolVersion` | Most recent active protocol version supported for the ACS URL | Variable, 5-8 characters; String; active versions refer to Table 1.5 | `R` | [EMV 3DS v2.2.0, section A.5.7, p. 232, Table A.6; section 1.7, p. 27, Table 1.5; no local numbered Req] |
| ACS Information Indicator / `acsInfoInd` | Array listing every applicable feature value for the card range | Array of 2-character Strings; values below | `O` | [EMV 3DS v2.2.0, section A.5.7, p. 232, Table A.6; no local numbered Req] |
| ACS Start Protocol Version / `acsStartProtocolVersion` | Earliest (oldest) active protocol version supported by the ACS | Variable, 5-8 characters; String; active versions refer to Table 1.5 | `R` | [EMV 3DS v2.2.0, section A.5.7, p. 233, Table A.6; section 1.7, p. 27, Table 1.5; no local numbered Req] |
| Action Indicator / `actionInd` | Action to take with the card range; ranges are processed in returned order | 1-character String; values below | `O` | [EMV 3DS v2.2.0, section A.5.7, p. 233, Table A.6; no local numbered Req] |
| DS End Protocol Version / `dsEndProtocolVersion` | Most recent active protocol version supported for the DS | Variable, 5-8 characters; String | `O` | [EMV 3DS v2.2.0, section A.5.7, p. 233, Table A.6; no local numbered Req] |
| DS Start Protocol Version / `dsStartProtocolVersion` | Earliest (oldest) active protocol version supported by the DS | Variable, 5-8 characters; String | `O` | [EMV 3DS v2.2.0, section A.5.7, p. 233, Table A.6; no local numbered Req] |
| End Card Range / `endRange` | End of the card range | String, 13-19 characters | `R` | [EMV 3DS v2.2.0, section A.5.7, p. 233, Table A.6; no local numbered Req] |
| Start Card Range / `startRange` | Start of the card range | String, 13-19 characters | `R` | [EMV 3DS v2.2.0, section A.5.7, p. 233, Table A.6; no local numbered Req] |

#### ACS Information Indicator values - `acsInfoInd`

| Code / range | Exact meaning or reservation status | Citation |
|---|---|---|
| `01` | Authentication Available at ACS | [EMV 3DS v2.2.0, section A.5.7, p. 232, Table A.6; no local numbered Req] |
| `02` | Attempts Supported by ACS or DS | [EMV 3DS v2.2.0, section A.5.7, p. 232, Table A.6; no local numbered Req] |
| `03` | Decoupled Authentication Supported | [EMV 3DS v2.2.0, section A.5.7, p. 232, Table A.6; no local numbered Req] |
| `04` | Whitelisting Supported | [EMV 3DS v2.2.0, section A.5.7, p. 232, Table A.6; no local numbered Req] |
| `05-79` | Reserved for EMVCo future use; values invalid until defined by EMVCo | [EMV 3DS v2.2.0, section A.5.7, p. 232, Table A.6; no local numbered Req] |
| `80-99` | Reserved for DS use | [EMV 3DS v2.2.0, section A.5.7, p. 232, Table A.6; no local numbered Req] |

#### Action Indicator values - `actionInd`

| Code | Exact meaning and condition | Citation |
|---|---|---|
| `A` | Add the card range to the cache; default value. When PReq omits Serial Number, every returned range is treated as `A` and the PRes Action Indicator is ignored. | [EMV 3DS v2.2.0, section A.5.7, p. 233, Table A.6; no local numbered Req] |
| `D` | Delete the card range from the cache. | [EMV 3DS v2.2.0, section A.5.7, p. 233, Table A.6; no local numbered Req] |
| `M` | Modify card-range data only; it must not modify Start Range or End Range. | [EMV 3DS v2.2.0, section A.5.7, p. 233, Table A.6; no local numbered Req] |

Table A.6 refers Start/End version fields to active versions in Table 1.5. That table marks `2.0.0` **Deprecated**, `2.1.0` **Active**, and `2.2.0` **Active**. This is version status, not a Card Range Data code range. [EMV 3DS v2.2.0, section 1.7, p. 27, Table 1.5; section A.5.7, pp. 232-233, Table A.6]

## 3. Annex A.6 - Message Extension Data

### 3.1 Container structure and limits

[`messageExtension`](./12-data-elements-part-3.md) carries data not defined in the Core specification. The extension-defining party shall define the payload format. Examples include JSON objects, binary data, and single data elements. The Message Extension field is a JSON array of extension JSON objects; multiple objects may be present when required. At most 10 extension objects are supported, and the entire Message Extension data element has a maximum of `81920` characters. Each object shall contain `name`, `id`, `criticalityIndicator`, and `data`. [EMV 3DS v2.2.0, section A.6, p. 234; section A.4, p. 204, Table A.1; no local numbered Req]

Table A.1 makes the array conditional, under conditions set by each DS, in `AReq`, `ARes`, `CReq`, `CRes`, `PReq`, `PRes`, `RReq`, and `RRes` for App, Browser, and 3RI in PA and NPA. It is not listed for `Erro`. [EMV 3DS v2.2.0, section A.4, p. 204, Table A.1; no local numbered Req]

### 3.2 Table A.7 - required attributes

| Attribute | Exact meaning | Length / type / values | Inclusion | Citation |
|---|---|---|---|---|
| `criticalityIndicator` | Whether the recipient must understand the extension contents to interpret the entire message | Boolean; `true` or `false` | `R` | [EMV 3DS v2.2.0, section A.6.1, p. 236, Table A.7; no local numbered Req] |
| `data` | Data carried in the extension | JSON Object; variable length; maximum `8059` characters | `R` | [EMV 3DS v2.2.0, section A.6.1, p. 236, Table A.7; no local numbered Req] |
| `id` | Unique identifier for the extension | String; variable length; maximum 64 characters; Payment System Registered Application Provider Identifier (RID) required as prefix | `R` | [EMV 3DS v2.2.0, section A.6.1, p. 236, Table A.7; no local numbered Req] |
| `name` | Extension data-set name defined by the extension owner | String; variable length; maximum 64 characters | `R` | [EMV 3DS v2.2.0, section A.6.1, p. 236, Table A.7; no local numbered Req] |

The `8059`-character per-`data` limit and `81920`-character whole-array limit are different edits and must not be normalised to one another. The page-235 example shows three objects, two critical and one non-critical, but its identifiers and payloads are illustrative rather than registered values. [EMV 3DS v2.2.0, sections A.6-A.6.1, pp. 234-236, Table A.7; no local numbered Req]

### 3.3 Identification

Every Message Extension defined for 3-D Secure must have a unique identifier. Examples of unique identifiers are EMVCo-assigned IDs, Object IDs (OID), Uniform Resource Identifiers (URI), and DS-assigned IDs. The extension-defining party specifies the identifier's format and value. Table A.7 separately requires a Payment System RID prefix; how that prefix applies to all four example identifier families is unresolved and must come from the applicable registered extension definition. [EMV 3DS v2.2.0, sections A.6.1-A.6.2, pp. 236-237, Table A.7; no local numbered Req]

### 3.4 Criticality, handling, validation, and errors

| Condition | Required recipient behavior | Error behavior | Citation |
|---|---|---|---|
| Extension affects the meaning of the rest of the message such that the message is understandable only in its context | Extension is critical and `criticalityIndicator` must be `true` | None merely because it is critical | [EMV 3DS v2.2.0, section A.6.3, p. 237; no local numbered Req] |
| Critical extension is recognised | Recipient must recognise and be able to process it | Process under the registered extension definition | [EMV 3DS v2.2.0, section A.6.3, p. 237; no local numbered Req] |
| Critical extension is not recognised | Treat the entire message as invalid | Return Error Code `202`; Error Detail identifies the unrecognised extension ID(s), comma-delimited when more than one | [EMV 3DS v2.2.0, section A.6.3, p. 237; section A.5.5, p. 228, Table A.4; no local numbered Req] |
| Non-critical extension is not recognised | Ignore the extension data and pass it to the destination system unaltered | Do not return `202` merely because it is unrecognised | [EMV 3DS v2.2.0, section A.6.3, p. 237; no local numbered Req] |
| Critical extension assignment | Every critical Message Extension shall be assigned by EMVCo | A non-EMVCo assignment must not be treated as an authorised critical extension | [EMV 3DS v2.2.0, section A.6.3, p. 237; no local numbered Req] |

All four Table A.7 attributes are `R`. Table A.4's nested-field example and the general Annex A missing/invalid edits support Error Code `201` for a missing required child and `203` for an invalid present child. The scoped A.6 pages explicitly name only `202`; they do not assign a separate code for more than 10 objects, a non-unique `id`, or whole-array/per-object length overflow. Those cases must not be mapped to a new extension-specific error code that the source does not state. [EMV 3DS v2.2.0, sections A.1-A.2, p. 147; section A.5.5, p. 228, Table A.4; sections A.6-A.6.3, pp. 234-237, Table A.7; no local numbered Req]

### 3.5 ACS implementation considerations

These are non-normative issuer ACS implementation considerations, not additional EMV 3DS requirements:

- Maintain a registry keyed by the exact extension `id`, with the registered owner, criticality permission, expected `name`, schema/format, supported messages/versions, and applicable DS rules. The Core pages do not supply that registry. [Protocol anchor: EMV 3DS v2.2.0, sections A.6-A.6.3, pp. 234-237, Table A.7]
- Enforce the exact object count, total length, attribute presence/type/length, and criticality rules before interpreting extension-specific data. Do not treat `name` as a substitute for the unique `id`. [Protocol anchor: EMV 3DS v2.2.0, sections A.6-A.6.3, pp. 234-237, Table A.7]
- Preserve an unrecognised non-critical extension unaltered when forwarding it. Parsing and reserialising JSON can change representation; the source states the preservation outcome but does not prescribe a byte-preserving implementation. [Protocol anchor: EMV 3DS v2.2.0, section A.6.3, p. 237]
- Do not invent binary encoding, payload schemas, privacy classifications, logging rules, or business meaning. Section A.6 expressly assigns the format to the extension-defining party. [Protocol anchor: EMV 3DS v2.2.0, section A.6, p. 234]

## 4. ACS-relevant searchable code index

This index consolidates high-value ACS routing, authentication, challenge, status, reason, and result values from Table A.1. It links back to the controlling data-element register; it does not extend Annex A.5/A.6 or analyse Annex A.7.

### 4.1 Device channel, message category, and protocol version

| Element | Code | Exact meaning / status | Condition or reservation | Source register and citation |
|---|---|---|---|---|
| `deviceChannel` | `01` | App-based | Valid defined value | [Part 3](./12-data-elements-part-3.md) - [EMV 3DS v2.2.0, section A.4, p. 195, Table A.1] |
| `deviceChannel` | `02` | Browser | Valid defined value | [Part 3](./12-data-elements-part-3.md) - [EMV 3DS v2.2.0, section A.4, p. 195, Table A.1] |
| `deviceChannel` | `03` | 3DS Requestor Initiated | Valid defined value | [Part 3](./12-data-elements-part-3.md) - [EMV 3DS v2.2.0, section A.4, p. 195, Table A.1] |
| `deviceChannel` | `04-79` | Reserved for future EMVCo use | Invalid until defined | [Part 3](./12-data-elements-part-3.md) - [EMV 3DS v2.2.0, section A.4, p. 195, Table A.1] |
| `deviceChannel` | `80-99` | Reserved for DS use | DS-defined | [Part 3](./12-data-elements-part-3.md) - [EMV 3DS v2.2.0, section A.4, p. 195, Table A.1] |
| `messageCategory` | `01` | Payment Authentication (PA) | Valid defined value | [Part 3](./12-data-elements-part-3.md) - [EMV 3DS v2.2.0, section A.4, p. 204, Table A.1] |
| `messageCategory` | `02` | Non-Payment Authentication (NPA) | Valid defined value | [Part 3](./12-data-elements-part-3.md) - [EMV 3DS v2.2.0, section A.4, p. 204, Table A.1] |
| `messageCategory` | `03-79` | Reserved for future EMVCo use | Invalid until defined | [Part 3](./12-data-elements-part-3.md) - [EMV 3DS v2.2.0, section A.4, p. 204, Table A.1] |
| `messageCategory` | `80-99` | Reserved for DS use | DS-defined | [Part 3](./12-data-elements-part-3.md) - [EMV 3DS v2.2.0, section A.4, p. 204, Table A.1] |
| `messageVersion` | `2.0.0` | Deprecated | Table 1.5 status | [Part 4](./13-data-elements-part-4.md) - [EMV 3DS v2.2.0, section 1.7, p. 27, Table 1.5; section A.4, p. 205, Table A.1] |
| `messageVersion` | `2.1.0` | Active | Table 1.5 status | [Part 4](./13-data-elements-part-4.md) - [EMV 3DS v2.2.0, section 1.7, p. 27, Table 1.5; section A.4, p. 205, Table A.1] |
| `messageVersion` | `2.2.0` | Active | Table 1.5 status | [Part 4](./13-data-elements-part-4.md) - [EMV 3DS v2.2.0, section 1.7, p. 27, Table 1.5; section A.4, p. 205, Table A.1] |

### 4.2 Authentication values

#### 3DS Requestor Authentication Indicator - `threeDSRequestorAuthenticationInd`

Required in App/Browser AReq and used by the ACS to choose handling. [Data Elements Part 1](./10-data-elements-part-1.md)

| Code / range | Exact meaning or reservation status | Citation |
|---|---|---|
| `01` | Payment transaction | [EMV 3DS v2.2.0, section A.4, p. 150, Table A.1] |
| `02` | Recurring transaction | [EMV 3DS v2.2.0, section A.4, p. 150, Table A.1] |
| `03` | Instalment transaction | [EMV 3DS v2.2.0, section A.4, p. 150, Table A.1] |
| `04` | Add card | [EMV 3DS v2.2.0, section A.4, p. 150, Table A.1] |
| `05` | Maintain card | [EMV 3DS v2.2.0, section A.4, p. 150, Table A.1] |
| `06` | Cardholder verification as part of EMV token ID&V | [EMV 3DS v2.2.0, section A.4, p. 150, Table A.1] |
| `07-79` | Reserved for EMVCo future use; invalid until defined | [EMV 3DS v2.2.0, section A.4, p. 150, Table A.1] |
| `80-99` | Reserved for DS use | [EMV 3DS v2.2.0, section A.4, p. 150, Table A.1] |

#### 3DS Requestor Authentication Method Verification Indicator - `threeDSReqAuthMethodInd`

The DS supplies this conditionally in App/Browser AReq, based on DS rules, to report its signature-verification result for the mechanism used by the Cardholder to authenticate to the 3DS Requestor. [Data Elements Part 1](./10-data-elements-part-1.md)

| Code / range | Exact meaning or reservation status | Citation |
|---|---|---|
| `01` | Verified | [EMV 3DS v2.2.0, section A.4, p. 151, Table A.1] |
| `02` | Failed | [EMV 3DS v2.2.0, section A.4, p. 151, Table A.1] |
| `03` | Not Performed | [EMV 3DS v2.2.0, section A.4, p. 151, Table A.1] |
| `04-79` | Reserved for EMVCo future use; invalid until defined | [EMV 3DS v2.2.0, section A.4, p. 151, Table A.1] |
| `80-99` | Reserved for DS use | [EMV 3DS v2.2.0, section A.4, p. 151, Table A.1] |

#### Authentication Method - `authenticationMethod`

The ACS sends this conditionally in RReq; it is not passed in the DS-to-3DS Server RReq, and in 3RI it is present only for Decoupled Authentication. [Data Elements Part 2](./11-data-elements-part-2.md)

| Code / range | Exact meaning or reservation status | Citation |
|---|---|---|
| `01` | Static Passcode | [EMV 3DS v2.2.0, section A.4, p. 167, Table A.1] |
| `02` | SMS OTP | [EMV 3DS v2.2.0, section A.4, p. 167, Table A.1] |
| `03` | Key fob or EMV card reader OTP | [EMV 3DS v2.2.0, section A.4, p. 167, Table A.1] |
| `04` | App OTP | [EMV 3DS v2.2.0, section A.4, p. 167, Table A.1] |
| `05` | OTP Other | [EMV 3DS v2.2.0, section A.4, p. 167, Table A.1] |
| `06` | KBA | [EMV 3DS v2.2.0, section A.4, p. 167, Table A.1] |
| `07` | OOB Biometrics | [EMV 3DS v2.2.0, section A.4, p. 167, Table A.1] |
| `08` | OOB Login | [EMV 3DS v2.2.0, section A.4, p. 167, Table A.1] |
| `09` | OOB Other | [EMV 3DS v2.2.0, section A.4, p. 167, Table A.1] |
| `10` | Other | [EMV 3DS v2.2.0, section A.4, p. 167, Table A.1] |
| `11` | Push Confirmation | [EMV 3DS v2.2.0, section A.4, p. 167, Table A.1] |
| `12-79` | Reserved for future EMVCo use; invalid until defined | [EMV 3DS v2.2.0, section A.4, p. 167, Table A.1] |
| `80-99` | Reserved for DS use | [EMV 3DS v2.2.0, section A.4, p. 167, Table A.1] |

#### Authentication Type - `authenticationType`

The ACS includes it in ARes when `transStatus` is `C` or `D`, and in RReq when `transStatus` is `Y` or `N`. [Data Elements Part 2](./11-data-elements-part-2.md)

| Code / range | Exact meaning or reservation status | Citation |
|---|---|---|
| `01` | Static | [EMV 3DS v2.2.0, section A.4, p. 168, Table A.1] |
| `02` | Dynamic | [EMV 3DS v2.2.0, section A.4, p. 168, Table A.1] |
| `03` | OOB | [EMV 3DS v2.2.0, section A.4, p. 168, Table A.1] |
| `04` | Decoupled | [EMV 3DS v2.2.0, section A.4, p. 168, Table A.1] |
| `05-79` | Reserved for future EMVCo use; invalid until defined | [EMV 3DS v2.2.0, section A.4, p. 168, Table A.1] |
| `80-99` | Reserved for DS use | [EMV 3DS v2.2.0, section A.4, p. 168, Table A.1] |

### 4.3 Challenge values

#### 3DS Requestor Challenge Indicator - `threeDSRequestorChallengeInd`

This optional App/Browser AReq field defaults at the ACS to `01` when absent. [Data Elements Part 1](./10-data-elements-part-1.md)

| Code / range | Exact meaning or reservation status | Citation |
|---|---|---|
| `01` | No preference | [EMV 3DS v2.2.0, section A.4, pp. 152-153, Table A.1] |
| `02` | No challenge requested | [EMV 3DS v2.2.0, section A.4, pp. 152-153, Table A.1] |
| `03` | Challenge requested - 3DS Requestor preference | [EMV 3DS v2.2.0, section A.4, pp. 152-153, Table A.1] |
| `04` | Challenge requested - Mandate | [EMV 3DS v2.2.0, section A.4, pp. 152-153, Table A.1] |
| `05` | No challenge requested - transactional risk analysis already performed | [EMV 3DS v2.2.0, section A.4, pp. 152-153, Table A.1] |
| `06` | No challenge requested - Data share only | [EMV 3DS v2.2.0, section A.4, pp. 152-153, Table A.1] |
| `07` | No challenge requested - strong consumer authentication already performed | [EMV 3DS v2.2.0, section A.4, pp. 152-153, Table A.1] |
| `08` | No challenge requested - utilise whitelist exemption if no challenge required | [EMV 3DS v2.2.0, section A.4, pp. 152-153, Table A.1] |
| `09` | Challenge requested - whitelist prompt requested if challenge required | [EMV 3DS v2.2.0, section A.4, pp. 152-153, Table A.1] |
| `10-79` | Reserved for EMVCo future use; invalid until defined | [EMV 3DS v2.2.0, section A.4, pp. 152-153, Table A.1] |
| `80-99` | Reserved for DS use | [EMV 3DS v2.2.0, section A.4, pp. 152-153, Table A.1] |

#### ACS challenge mandate - `acsChallengeMandated`

The ACS supplies this conditionally in ARes when `transStatus` is `C` or `D`. It applies to App, Browser, and 3RI for PA and NPA. [Data Elements Part 1](./10-data-elements-part-1.md)

| Code | Exact meaning and condition | Citation |
|---|---|---|
| `Y` | Challenge is mandated | [EMV 3DS v2.2.0, section A.4, p. 160, Table A.1] |
| `N` | Challenge is not mandated | [EMV 3DS v2.2.0, section A.4, p. 160, Table A.1] |

No reserved or future-use range is printed for `acsChallengeMandated`.

#### ACS UI Type - `acsUiType`

The ACS supplies this required App CRes field to identify the UI type the 3DS SDK renders. [Data Elements Part 1](./10-data-elements-part-1.md)

| Code / range | Exact meaning or reservation status | Citation |
|---|---|---|
| `01` | Text | [EMV 3DS v2.2.0, section A.4, p. 165, Table A.1] |
| `02` | Single Select | [EMV 3DS v2.2.0, section A.4, p. 165, Table A.1] |
| `03` | Multi Select | [EMV 3DS v2.2.0, section A.4, p. 165, Table A.1] |
| `04` | OOB | [EMV 3DS v2.2.0, section A.4, p. 165, Table A.1] |
| `05` | HTML | [EMV 3DS v2.2.0, section A.4, p. 165, Table A.1] |
| `06-79` | Reserved for EMVCo future use; invalid until defined | [EMV 3DS v2.2.0, section A.4, p. 165, Table A.1] |
| `80-99` | Reserved for DS use | [EMV 3DS v2.2.0, section A.4, p. 165, Table A.1] |

#### Challenge Window Size - `challengeWindowSize`

The Requestor supplies this required Browser CReq value, and the ACS shall format its content to render appropriately in the selected window. [Data Elements Part 3](./12-data-elements-part-3.md)

| Code | Exact meaning | Citation |
|---|---|---|
| `01` | `250 x 400` | [EMV 3DS v2.2.0, section A.4, p. 195, Table A.1] |
| `02` | `390 x 400` | [EMV 3DS v2.2.0, section A.4, p. 195, Table A.1] |
| `03` | `500 x 600` | [EMV 3DS v2.2.0, section A.4, p. 195, Table A.1] |
| `04` | `600 x 400` | [EMV 3DS v2.2.0, section A.4, p. 195, Table A.1] |
| `05` | Full screen | [EMV 3DS v2.2.0, section A.4, p. 195, Table A.1] |

No reserved or future-use range is printed for `challengeWindowSize`.

#### Challenge Cancelation Indicator - `challengeCancel`

The SDK sends it conditionally in App CReq and the ACS sends it conditionally in RReq. Values `04` or `05` are required when `transStatusReason = 14`. [Data Elements Part 3](./12-data-elements-part-3.md)

| Code / range | Exact meaning or reservation status | Citation |
|---|---|---|
| `01` | Cardholder selected Cancel | [EMV 3DS v2.2.0, section A.4, p. 189, Table A.1] |
| `02` | Reserved for future EMVCo use; invalid until defined | [EMV 3DS v2.2.0, section A.4, p. 189, Table A.1] |
| `03` | Transaction timed out - Decoupled Authentication | [EMV 3DS v2.2.0, section A.4, p. 189, Table A.1] |
| `04` | Transaction timed out at ACS - other timeouts | [EMV 3DS v2.2.0, section A.4, p. 189, Table A.1] |
| `05` | Transaction timed out at ACS - first CReq not received | [EMV 3DS v2.2.0, section A.4, p. 189, Table A.1] |
| `06` | Transaction error | [EMV 3DS v2.2.0, section A.4, p. 189, Table A.1] |
| `07` | Unknown | [EMV 3DS v2.2.0, section A.4, p. 189, Table A.1] |
| `08` | Transaction timed out at SDK | [EMV 3DS v2.2.0, section A.4, p. 189, Table A.1] |
| `09-79` | Reserved for future EMVCo use; invalid until defined | [EMV 3DS v2.2.0, section A.4, p. 189, Table A.1] |
| `80-99` | Reserved for future DS use | [EMV 3DS v2.2.0, section A.4, p. 189, Table A.1] |

#### Challenge Completion Indicator - `challengeCompletionInd`

| Code | Exact meaning and condition | Source register and citation |
|---|---|---|
| `Y` | Challenge completed; no further challenge-message exchanges required. ACS will populate Transaction Status in CRes. | [Part 3](./12-data-elements-part-3.md) - [EMV 3DS v2.2.0, section A.4, p. 190, Table A.1] |
| `N` | Challenge not completed; additional challenge-message exchanges required. | [Part 3](./12-data-elements-part-3.md) - [EMV 3DS v2.2.0, section A.4, p. 190, Table A.1] |

### 4.4 Transaction status, reason, and result values

#### Transaction Status - `transStatus`

The field is defined in [Data Elements Part 4](./13-data-elements-part-4.md). For PA it is required in ARes, RReq, and final CRes; NPA conditions are DS-defined. Final CRes permits only `Y` or `N`, and `C` is invalid when Requestor Challenge Indicator is `06`. The message-specific validity rules that follow the A.7 boundary are not reproduced or analysed here. [EMV 3DS v2.2.0, section A.4, pp. 216-217, Table A.1; no local numbered Req]

| Code | Exact meaning | Message/conditional meaning | Citation |
|---|---|---|---|
| `Y` | Authentication/account verification successful | Permitted final CRes value | [EMV 3DS v2.2.0, section A.4, pp. 216-217, Table A.1] |
| `N` | Not authenticated/not verified; transaction denied | Permitted final CRes value | [EMV 3DS v2.2.0, section A.4, pp. 216-217, Table A.1] |
| `U` | Authentication/account verification could not be performed because of technical or other problem | Problem is indicated in ARes or RReq; not permitted in final CRes | [EMV 3DS v2.2.0, section A.4, pp. 216-217, Table A.1] |
| `A` | Attempts processing performed; proof of attempt, not authentication/verification | Not permitted in final CRes | [EMV 3DS v2.2.0, section A.4, pp. 216-217, Table A.1] |
| `C` | Challenge required through CReq/CRes | Invalid when Requestor Challenge Indicator is `06`; not permitted in final CRes | [EMV 3DS v2.2.0, section A.4, pp. 216-217, Table A.1] |
| `D` | Challenge required; Decoupled Authentication confirmed | Not permitted in final CRes | [EMV 3DS v2.2.0, section A.4, pp. 216-217, Table A.1] |
| `R` | Issuer rejects authentication/verification and requests no authorisation attempt | Not permitted in final CRes | [EMV 3DS v2.2.0, section A.4, pp. 216-217, Table A.1] |
| `I` | Informational only; acknowledges Requestor challenge preference | Not permitted in final CRes | [EMV 3DS v2.2.0, section A.4, pp. 216-217, Table A.1] |

No reserved or future-use range is printed for `transStatus`; every one-character value above remains semantically distinct.

#### Transaction Status Reason - `transStatusReason`

The field is defined in [Data Elements Part 4](./13-data-elements-part-4.md). For PA it is required in ARes/RReq when `transStatus` is `N`, `U`, or `R`; NPA inclusion is DS-defined. [EMV 3DS v2.2.0, section A.4, pp. 218-220, Table A.1; no local numbered Req]

| Code / range | Exact meaning or reservation status | Citation |
|---|---|---|
| `01` | Card authentication failed | [EMV 3DS v2.2.0, section A.4, p. 218, Table A.1] |
| `02` | Unknown device | [EMV 3DS v2.2.0, section A.4, p. 218, Table A.1] |
| `03` | Unsupported device | [EMV 3DS v2.2.0, section A.4, p. 218, Table A.1] |
| `04` | Exceeds authentication frequency limit | [EMV 3DS v2.2.0, section A.4, p. 218, Table A.1] |
| `05` | Expired card | [EMV 3DS v2.2.0, section A.4, p. 218, Table A.1] |
| `06` | Invalid card number | [EMV 3DS v2.2.0, section A.4, p. 218, Table A.1] |
| `07` | Invalid transaction | [EMV 3DS v2.2.0, section A.4, p. 218, Table A.1] |
| `08` | No card record | [EMV 3DS v2.2.0, section A.4, p. 218, Table A.1] |
| `09` | Security failure | [EMV 3DS v2.2.0, section A.4, p. 218, Table A.1] |
| `10` | Stolen card | [EMV 3DS v2.2.0, section A.4, p. 218, Table A.1] |
| `11` | Suspected fraud | [EMV 3DS v2.2.0, section A.4, p. 218, Table A.1] |
| `12` | Transaction not permitted to Cardholder | [EMV 3DS v2.2.0, section A.4, p. 218, Table A.1] |
| `13` | Cardholder not enrolled | [EMV 3DS v2.2.0, section A.4, p. 218, Table A.1] |
| `14` | Transaction timed out at ACS | [EMV 3DS v2.2.0, section A.4, p. 218, Table A.1] |
| `15` | Low confidence | [EMV 3DS v2.2.0, section A.4, p. 218, Table A.1] |
| `16` | Medium confidence | [EMV 3DS v2.2.0, section A.4, p. 218, Table A.1] |
| `17` | High confidence | [EMV 3DS v2.2.0, section A.4, p. 219, Table A.1] |
| `18` | Very high confidence | [EMV 3DS v2.2.0, section A.4, p. 219, Table A.1] |
| `19` | Exceeds ACS maximum challenges | [EMV 3DS v2.2.0, section A.4, p. 219, Table A.1] |
| `20` | Non-payment transaction not supported | [EMV 3DS v2.2.0, section A.4, p. 219, Table A.1] |
| `21` | 3RI transaction not supported | [EMV 3DS v2.2.0, section A.4, p. 219, Table A.1] |
| `22` | ACS technical issue | [EMV 3DS v2.2.0, section A.4, p. 219, Table A.1] |
| `23` | Decoupled Authentication required by ACS but not requested | [EMV 3DS v2.2.0, section A.4, p. 219, Table A.1] |
| `24` | 3DS Requestor Decoupled Max Expiry Time exceeded | [EMV 3DS v2.2.0, section A.4, p. 219, Table A.1] |
| `25` | Insufficient Decoupled time, so ACS will not attempt authentication | [EMV 3DS v2.2.0, section A.4, p. 219, Table A.1] |
| `26` | Authentication attempted but not performed by Cardholder | [EMV 3DS v2.2.0, section A.4, p. 219, Table A.1] |
| `27-79` | Reserved for future EMVCo use; invalid until defined | [EMV 3DS v2.2.0, section A.4, pp. 219-220, Table A.1] |
| `80-99` | Reserved for DS use | [EMV 3DS v2.2.0, section A.4, p. 220, Table A.1] |

#### Results Message Status - `resultsStatus`

This required RRes field is defined in [Data Elements Part 4](./13-data-elements-part-4.md). It describes RReq receipt/processing, not the Cardholder authentication result. [EMV 3DS v2.2.0, section A.4, p. 211, Table A.1; section B.9, p. 269, Table B.9; no local numbered Req]

| Code / range | Exact meaning or reservation status | Citation |
|---|---|---|
| `01` | RReq received for further processing | [EMV 3DS v2.2.0, section A.4, p. 211, Table A.1] |
| `02` | CReq not sent to ACS by the 3DS Requestor; 3DS Server or Requestor opted out of challenge | [EMV 3DS v2.2.0, section A.4, p. 211, Table A.1] |
| `03` | ARes with Transaction Status `C` or `D` was not delivered to the Requestor because of technical error | [EMV 3DS v2.2.0, section A.4, p. 211, Table A.1] |
| `04-79` | Reserved for future EMVCo use; invalid until defined | [EMV 3DS v2.2.0, section A.4, p. 211, Table A.1] |
| `80-99` | Reserved for DS use | [EMV 3DS v2.2.0, section A.4, p. 211, Table A.1] |

## 5. ACS traceability and interpretation boundary

| ACS behavior area | Source-prescribed effect | Evidence |
|---|---|---|
| Browser capability input | ACS receives transaction-specific Browser AReq information; 3DS Server is responsible for accuracy, non-alteration, non-hard-coding, and transaction uniqueness. | [EMV 3DS v2.2.0, section A.5.2, p. 223; no local numbered Req] |
| 3DS Method correlation | ACS receives the notification URL and the AReq-correlated 3DS Server Transaction ID; completion returns the ID to the Requestor. | [EMV 3DS v2.2.0, section A.5.3, pp. 224-225, Table A.2; no local numbered Req] |
| Browser challenge envelope | ACS receives Base64url `creq`, preserves optional opaque `threeDSSessionData`, and returns Base64url `cres` with the opaque value unchanged. | [EMV 3DS v2.2.0, section A.5.4, p. 226, Table A.3; no local numbered Req] |
| Error production and consumption | ACS uses exact Table A.4 conditions/details and does not collapse missing, malformed, duplicate, transaction-state, decryption, timeout, and system failures into one generic result. | [EMV 3DS v2.2.0, section A.5.5, pp. 227-230, Table A.4; no local numbered Req] |
| Card-range capability interpretation | `acsInfoInd` can advertise ACS authentication, Attempts, Decoupled, and Whitelisting support; Start/End versions and range actions remain distinct fields. | [EMV 3DS v2.2.0, section A.5.7, pp. 232-233, Table A.6; no local numbered Req] |
| Extension validation | ACS enforces structure/limits and registered critical-extension processing; unknown critical extensions invalidate the message with `202`, while unknown non-critical data is ignored and passed onward unaltered. | [EMV 3DS v2.2.0, sections A.6-A.6.3, pp. 234-237, Table A.7; section A.5.5, p. 228, Table A.4; no local numbered Req] |

The specification does not define ACS internal APIs, extension registries, binary encodings, risk policies, retry algorithms, persistence, telemetry, or operator workflows in these pages. Those are implementation, scheme/vendor, or unresolved matters unless governed by another cited source.

## 6. Unresolved and externally dependent items in this batch

1. **Browser Information list omission and uniqueness wording.** A.5.2 omits `browserJavascriptEnabled` and says listed data is unique to each transaction even though values can naturally repeat. Preserve Table A.1 and do not fabricate varying browser data. [Source ambiguity] [EMV 3DS v2.2.0, section A.4, p. 172, Table A.1; section A.5.2, p. 223]
2. **Browser `cres` field-set reference.** Table A.3 points `cres` to Table B.4, while Req 138 points final Browser CRes to Table B.5. The field set requires authoritative clarification. [Source ambiguity] [EMV 3DS v2.2.0, section 3.3, p. 70, Req 138; section A.5.4, p. 226, Table A.3; sections B.4-B.5, pp. 265-266]
3. **`threeDSSessionData` format and unit wording.** Table A.3 prints both “Alphanumeric” and “Base64url encoded,” although Base64url can use `-` and `_`; its length cell says maximum 1024 while the note specifically limits the post-encoding field to 1024 bytes. Do not impose a narrower alphabet or a pre-encoding character limit without clarification. [Source/validation ambiguity] [EMV 3DS v2.2.0, section A.5.4, p. 226, Table A.3]
4. **Error Code `304` detail wording.** Inside the Error Detail column, Table A.4 says to list the invalid currency/exponent pair “as Error Description” and uses `Challenge Request.Purchase.currency` / `.exponent` rather than the Table A.1 field names. Preserve the printed text and obtain an authoritative field-path/output clarification. [Source ambiguity] [EMV 3DS v2.2.0, section A.5.5, p. 229, Table A.4; section A.4, p. 209, Table A.1]
5. **Duplicate and invalid-state error routing.** Table A.4 defines `204`, `301`, and `305`, but section 5.9 does not reconcile every duplicate/state condition with a message-specific route. Do not invent a universal replay or cached-response contract. [Source ambiguity / implementation dependency] [EMV 3DS v2.2.0, section 5.9, pp. 124-132; section A.5.5, pp. 228-229, Table A.4]
6. **DS Start meaning and nested inclusion.** Table A.1 calls `dsStartProtocolVersion` the most recent active version and prints PRes `R` for both DS version fields; Table A.6 calls Start the earliest/oldest and marks both nested fields `O`. Preserve both printed definitions until clarified. [Source ambiguity] [EMV 3DS v2.2.0, section A.4, p. 197, Table A.1; section A.5.7, pp. 232-233, Table A.6]
7. **Message Extension source role.** Table A.1 lists only 3DS Server as source while allowing the array in messages created by multiple components; A.6 assigns format/value decisions to the extension-defining party. Apply the registered extension and DS rules rather than inferring universal Server authorship. [Source ambiguity] [EMV 3DS v2.2.0, section A.4, p. 204, Table A.1; sections A.6-A.6.3, pp. 234-237, Table A.7]
8. **Message Extension ID prefix and identifier families.** Table A.7 requires a Payment System RID prefix, while A.6.2 allows EMVCo IDs, OIDs, URIs, and DS-assigned IDs and lets the defining party specify format/value. The composition rule and validation behavior for every family are not stated. [Source/profile dependency] [EMV 3DS v2.2.0, sections A.6.1-A.6.2, pp. 236-237, Table A.7]
9. **Extension-specific format and validation profile.** Binary representation, schemas, non-critical terminal-recipient handling, duplicate `id` handling, and a specific error mapping for count/length/uniqueness violations are not fully defined in A.6. Obtain the registered extension and DS profile; do not invent these rules. [Scheme/vendor and implementation dependency] [EMV 3DS v2.2.0, sections A.6-A.6.3, pp. 234-237, Table A.7]

## 7. Boundary and verification record

- Physical PDF pages 223-237 were rendered at readable resolution and directly inspected. The extracted text was compared against every page image.
- Table counts verified: Table A.2 - 2 fields; Table A.3 - 3 fields; Table A.4 - 18 exact Error Codes; Table A.5 - 11 ISO 4217 exclusions plus one ISO 3166-1 excluded range; Table A.6 - 9 fields; Table A.7 - 4 required attributes.
- Every numeric/string code and range in Tables A.4-A.7 and every ACS-focused linked Table A.1 code in section 4 was checked against the physical PDF, including linked pages 27, 150-153, 160, 165, 167-168, 189-190, 195, 204-205, 211, and 216-220. Distinct ranges, reserved values, future-use values, DS-reserved values, and the `2.0.0` Deprecated status were preserved verbatim in meaning.
- Page 223 starts A.5; pages 224-233 contain Tables A.2-A.6 and examples; page 234 starts A.6; pages 234-237 contain the extension rules and Table A.7.
- Physical page 237 starts A.7 after A.6.3. The A.7 heading and boundary were verified, but its substantive text was not analysed in this batch.
- Annex A.5-A.6.3 contains no local numbered requirements; none were invented.

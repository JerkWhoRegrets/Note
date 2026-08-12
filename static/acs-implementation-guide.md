# Issuer-Side ACS Implementation Guide

## Purpose, evidence boundary, and labels

This guide is an issuer-side synthesis of the verified analysis for **EMV 3-D Secure Protocol and Core Functions Specification v2.2.0**. The source PDF remains normative; this file does not replace it. Direct message routes are expanded in [`message-route-matrix.md`](message-route-matrix.md), link controls in [`security-matrix.md`](security-matrix.md), and verification-service design in [`verification-and-otp-design.md`](verification-and-otp-design.md).

Every statement below uses one of these labels:

- **Normative Requirement** — an explicit source requirement or normative table/processing rule. Its citation gives the section, physical PDF page, and requirement number when the source has one.
- **Interpretation** — a bounded conclusion drawn from cited protocol text or figures.
- **Issuer ACS Implementation Consideration** — a non-normative reference design. It must not be represented as an EMV 3DS requirement.
- **Scheme/Vendor/External Dependency** — behavior left to a DS, Payment System, SDK specification, CA, regulation, certification programme, issuer policy, or vendor profile.
- **Unresolved** — a source conflict or missing profile tracked in [`unresolved-questions.md`](unresolved-questions.md).

## 1. ACS scope, trust boundaries, and dependencies

### 1.1 Protocol role and ownership

**Normative Requirement.** The Issuer controls the ACS. The ACS contains the Issuer's authentication rules, determines whether authentication is available, authenticates the Cardholder or confirms account information, creates ARes/CRes/RReq as applicable, and may generate an Authentication Value. One logical ACS may be divided across several physical servers or account ranges. [EMV 3DS v2.2.0, sections 1.5 and 2.3.4, pp. 19, 23, and 34]

**Interpretation.** The ACS is a protocol component, not the total issuer authentication estate. OTP generators, delivery providers, issuer mobile apps, OOB authentication services, fraud engines, account systems, case management, audit platforms, and key-management systems are issuer or vendor dependencies unless a cited protocol message explicitly crosses their boundary. [EMV 3DS v2.2.0, sections 2.3.4 and 2.5.2, pp. 34 and 36; section 3.2, pp. 57-58; section 3.4, pp. 71 and 76, Figure 3.4, Req 330]

### 1.2 Direct trust boundaries

| Boundary | Direct parties | ACS responsibility in the Core protocol | What is not on that direct path | Classification and source |
|---|---|---|---|---|
| Server authentication | DS -> ACS for AReq; ACS -> DS for RReq; the corresponding response returns on the request/response link | Operate the ACS endpoint for AReq and the DS-facing client for RReq; validate and correlate responses | 3DS Server is reached through the DS, never by a direct ACS protocol hop | **Normative Requirement / Interpretation.** [EMV 3DS v2.2.0, sections 3.1, 3.3, and 3.4, pp. 47-56, 63-70, and 73-77, Reqs 21-24, 33-38, 62-75, 99-114, 124-137, 285-299, and 332-341; sections 6.1.2-6.1.3, pp. 135-136] |
| App challenge | 3DS SDK <-> ACS | Operate the ACS URL, complete the SDK secure channel, receive JWE CReq, and return JWE CRes | DS and 3DS Server are not on the direct challenge path | **Normative Requirement.** [EMV 3DS v2.2.0, section 3.1, pp. 50-56, Reqs 32, 42-61, and 76-78; section 5.7.1, p. 121, Reqs 253-254; section 6.1.4.1, p. 136; sections 6.2.3-6.2.4, pp. 141-144] |
| Browser challenge | Cardholder Browser <-> ACS, followed by Browser -> Notification URL supplied by the 3DS Server in the Requestor Environment; the terminating Requestor/3DS Server component is unresolved | Serve the challenge UI, process Browser form interactions, and return the final CRes through the Browser | DS and 3DS Server do not relay the Browser CReq or challenge HTML; the ACS does not make a direct server-to-server final-CRes POST. The source nevertheless describes the Browser return as reaching both the Requestor side and the 3DS Server, so the Server cannot be excluded as the Notification URL terminator | **Normative Requirement / Interpretation / Unresolved final carriage and recipient.** [EMV 3DS v2.2.0, section 3.3, pp. 66-70, Reqs 117-140; section 5.1.2, p. 112, Req 191; section 5.8.2, p. 123, Reqs 266-270; sections 5.9.6 and 5.9.12, pp. 127 and 131-132, source footnote 7; section A.4, p. 206, Table A.1; section A.5.4, p. 226, Table A.3] [Related unresolved questions](unresolved-questions.md#source-ambiguity) |
| 3DS Method | Cardholder Browser -> ACS 3DS Method URL or an entity designated by the ACS; Browser -> 3DS Method Notification URL at the 3DS Server/Requestor side | If the ACS supports 3DS Method, publish the Method URL, ensure the Browser-ACS Method link uses the specified server-authenticated TLS profile, and store applicable browser values under the same 3DS Server Transaction ID; how a designated entity gathers or returns those values is outside scope | The DS supplies cached routing metadata but is not on either Method POST | **Normative Requirement / Interpretation / External Dependency.** [EMV 3DS v2.2.0, section 3.3, p. 60, Reqs 83-85; section 5.8.1, pp. 122-123, Reqs 255-264 and 315; section 6.1.8, p. 137; sections A.5.3 and A.5.7, pp. 224-225 and 232-233, Tables A.2 and A.6] |
| Issuer verification | ACS <-> issuer authentication or service-provider components | Obtain a verification outcome that can drive protocol state | No EMV 3DS message names or private API shapes are defined for OTP generation, delivery, verification, OOB result return, or Decoupled Authentication Service integration | **Interpretation / Scheme/Vendor/External Dependency.** [EMV 3DS v2.2.0, section 3.1, pp. 50 and 53-54, Reqs 32 and 50-61; section 3.2, pp. 57-58; section 3.4, pp. 71 and 76, Figure 3.4, Req 330] |

### 1.3 Required external inputs and decisions

**Normative Requirement.** The ACS evaluates AReq data, uses the named Requestor challenge/Decoupled indicators as specified, and determines the transaction disposition. [EMV 3DS v2.2.0, section 3.1, pp. 48-50, Reqs 25-32, 318, and 386; section 3.3, pp. 64-65, Reqs 106-109; section 3.4, pp. 74-75, Reqs 287-294]

**Interpretation.** The authentication decisioning algorithm is outside the Core specification; the cited flow requirements constrain its inputs and protocol outputs but do not prescribe an issuer risk model. [EMV 3DS v2.2.0, section 3.3, p. 64, Req 107 and source footnote 3; section 2.5.2, p. 36]

**Scheme/Vendor/External Dependency.** Payment System or DS rules define such items as ECI, Authentication Value, Attempts support, some status/reason behavior, logging, and DS timeout values. [EMV 3DS v2.2.0, section 3.1, pp. 49-51, Reqs 31, 35, and 318; section 3.4, pp. 74-75, Reqs 290-292; sections 5.5.2-5.5.2.1, pp. 117-118, Reqs 228, 231, and 234]

**Issuer ACS Implementation Consideration.** Treat the following as separately governed dependencies: account eligibility, fraud/risk scoring, regulatory authentication policy, verification-method availability, credential/contact lookup, OTP or push delivery, Payment System output generation, DS routing/configuration, certificate trust, and incident/operations services. Record the owner and policy version for every dependency; do not encode uncited scheme behavior as a Core rule.

## 2. Endpoint sourcing and exact direct routes

### 2.1 Endpoint inventory

| Endpoint or URL | Protocol source/provider | Direct caller and purpose | ACS persistence obligation | Classification and source |
|---|---|---|---|---|
| ACS AReq endpoint | ACS; the DS routes AReq directly to the ACS | DS posts AReq; ACS returns ARes or Erro as the corresponding response | Protocol requires transaction state needed for the selected disposition; account-range onboarding and endpoint-registry design require the applicable DS profile | **Normative Requirement / Interpretation / Scheme Dependency.** [EMV 3DS v2.2.0, sections 3.1, 3.3, and 3.4, pp. 47-50, 63-65, and 73-75, Reqs 21-34, 99-109, and 285-294; section 6.1.3.1, p. 136] |
| `acsURL` | ACS; returned in ARes for challenge | SDK or Browser posts CReq directly to the ACS | Store or resolve it consistently across logical ACS partitions; the Core specification does not prescribe the registry | **Normative Requirement / Issuer ACS Implementation Consideration.** [EMV 3DS v2.2.0, section 3.1, pp. 50-52, Reqs 32 and 42-46; section 3.3, pp. 64-67, Reqs 109, 117, and 119-121; section A.4, pp. 165-166, Table A.1] |
| `dsURL` | DS; supplied to ACS on the DS-to-ACS AReq leg | ACS posts RReq directly to the DS | The ACS is expressly responsible for storing this URL for later RReq delivery | **Normative Requirement.** [EMV 3DS v2.2.0, sections 3.1 and 3.3, pp. 48-50 and 63-65, Reqs 24, 32, 99, and 109; section A.4, p. 198, Table A.1] |
| `threeDSServerURL` | 3DS Server; carried in AReq and retained by the DS | DS posts RReq directly to the 3DS Server | DS retains and uses this routing value on the RReq forwarding leg; the source does not define operational ownership/custody beyond those roles | **Normative Requirement / Interpretation.** [EMV 3DS v2.2.0, sections 3.1, 3.3, and 3.4, pp. 47-48, 63, 72-73, and 77, Reqs 13, 22, 98-99, 277, and 333; section A.4, p. 157, Table A.1] |
| Browser `notificationURL` | Supplied by the 3DS Server in AReq; located in the Requestor Environment; terminating Requestor/3DS Server component unresolved | ACS causes final Base64url CRes to be returned through the Browser to this URL | Preserve it for Browser result return; validity-dependent error branches and the terminating component are not fully reconciled | **Normative Requirement / Unresolved.** [EMV 3DS v2.2.0, section 3.3, pp. 63 and 66-70, Reqs 98, 117, and 138-140; sections 5.9.6 and 5.9.12, pp. 127-128 and 131-132, source footnote 7; section A.4, p. 206, Table A.1; section A.5.4, p. 226, Table A.3] |
| 3DS Method URL | ACS when it supports the Method; advertised for an account range in PRes/Card Range Data; the flow overview permits an ACS-designated Method entity | Browser posts Base64url `threeDSMethodData` before AReq | ACS stores applicable values under the supplied 3DS Server Transaction ID for the later AReq; any designated-entity retrieval/integration is outside scope | **Normative Requirement / External Dependency.** [EMV 3DS v2.2.0, section 3.3, p. 60, Reqs 83-85; section 5.8.1, pp. 122-123, Reqs 255-264; section 6.1.8, p. 137; sections A.5.3 and A.5.7, pp. 224-225 and 232, Tables A.2 and A.6] |
| 3DS Method Notification URL | 3DS Server/Requestor side; included in the initial `threeDSMethodData` | Browser posts returned `threeDSMethodData` after ACS Method processing; Req 263 requires the recalled 3DS Server Transaction ID, while Annex A.5.3 Example 2 shows Base64url JSON but the requirement does not restate that encoding | ACS must recall the received transaction ID. The conclusion that no ACS callback client exists follows from the Browser-carried return route rather than from a separately stated interface rule | **Normative Requirement / Interpretation / Unresolved completion encoding.** [EMV 3DS v2.2.0, section 5.8.1, pp. 122-123, Reqs 259-263; section A.5.3, pp. 224-225, Table A.2] |

### 2.2 Message routes

**Interpretation.** The exact application paths are:

1. `AReq`: 3DS Server -> DS, then DS -> ACS. `ARes`: ACS -> DS as the HTTP response, then DS -> 3DS Server as the HTTP response on the other direct leg. [EMV 3DS v2.2.0, sections 3.1, 3.3, and 3.4, pp. 47-52, 63-66, and 72-76, Reqs 14-39, 98-117, 277-283, and 285-299]
2. App `CReq/CRes`: SDK -> ACS and ACS -> SDK directly; several pairs may occur. [EMV 3DS v2.2.0, section 3.1, pp. 52-56, Reqs 45-61 and 76-78; section 5.7.1, p. 121, Reqs 253-254]
3. Browser challenge: one protocol CReq is posted Browser -> ACS; the ACS and Browser may then exchange several HTML/form interactions. After RReq/RRes, final CRes is posted ACS -> Browser -> supplied Notification URL in the Requestor Environment; the source does not unambiguously select the terminating Requestor/3DS Server component. [EMV 3DS v2.2.0, section 3.3, pp. 66-70, Reqs 117-140; section 5.8.2, p. 123, Reqs 267-270; sections 5.9.6 and 5.9.12, pp. 127 and 131-132, source footnote 7; section A.4, p. 206, Table A.1; section A.5.4, p. 226, Table A.3]
4. `RReq`: ACS -> DS, then DS -> 3DS Server. `RRes`: 3DS Server -> DS as the response, then DS -> ACS as the response. [EMV 3DS v2.2.0, section 3.1, pp. 54-56, Reqs 62-75; section 3.3, pp. 68-70, Reqs 124-137; section 3.4, pp. 76-77, Reqs 332-341 and 349-353]
5. `Erro` travels directly from the component detecting the error to the immediate protocol peer identified by the receiver-specific branch; it is not a broadcast. The concrete variations are listed in [`message-route-matrix.md`](message-route-matrix.md). [EMV 3DS v2.2.0, section 5.9, pp. 124-132; section A.5.5, pp. 227-230, Table A.4; section B.10, p. 270, Table B.10]
6. `PReq/PRes` is a direct 3DS Server <-> DS preparation exchange. The ACS is not on that path; it depends indirectly on the resulting version/card-range/3DS Method metadata. [EMV 3DS v2.2.0, section 5.6, pp. 119-121, Reqs 246-251, 303-304, and 385; section A.5.7, pp. 232-233, Table A.6]

## 3. Transaction identity, correlation, persistence, and idempotency

### 3.1 Protocol identifier generation and correlation

| Identifier | Generator/source | Required protocol scope | Normative persistence or matching point | Source |
|---|---|---|---|---|
| `threeDSServerTransID` | 3DS Server | Required in AReq/ARes/CReq/CRes/PReq/PRes/RReq/RRes and conditionally available in Erro; canonical UUID, 36 characters | Same value correlates 3DS Method with the later AReq; ACS stores it for App/Browser challenge and Decoupled result processing | **Normative Requirement.** [EMV 3DS v2.2.0, section 3.1, p. 50, Req 32; section 3.3, pp. 59-60 and 64-65, Reqs 81-85 and 109; section 3.4, p. 75, Req 328; section 5.8.1, pp. 122-123, Reqs 259-263; section A.4, p. 157, Table A.1] |
| `dsTransID` | DS | Added by DS before AReq reaches ACS; required in ARes/RReq/RRes/PRes and conditionally available in Erro; canonical UUID, 36 characters | ACS stores it with the Server Transaction ID for later RReq | **Normative Requirement.** [EMV 3DS v2.2.0, section 3.1, pp. 47-50, Reqs 15 and 32; section 3.3, pp. 62 and 64-65, Reqs 94 and 109; section 3.4, pp. 73-75, Reqs 279 and 328; section A.4, p. 198, Table A.1] |
| `acsTransID` | ACS | Required in ARes/CReq/CRes/RReq/RRes and conditionally available in Erro; canonical UUID, 36 characters | ACS creates it for one transaction; the Core tables do not prescribe a database key or retention period | **Normative Requirement / Interpretation.** [EMV 3DS v2.2.0, section 3.1, p. 49, Req 27; section 3.3, p. 63, Req 104; section 3.4, p. 74, Req 288; section A.4, p. 164, Table A.1] |
| `sdkTransID` | 3DS SDK | App channel AReq/ARes/CReq/CRes/RReq/RRes and conditionally available in Erro; canonical UUID, 36 characters | ACS stores it for App CReq processing | **Normative Requirement.** [EMV 3DS v2.2.0, section 3.1, pp. 46 and 50, Reqs 2-6 and 32; section A.4, p. 214, Table A.1] |
| `messageVersion` | Originating 3DS Server | Remains consistent across the transaction | A response must use the request version and transaction/reference identifiers must match the request | **Normative Requirement.** [EMV 3DS v2.2.0, sections 5.1.4 and 5.1.6, pp. 112 and 114, Reqs 195, 212, 311, and 320; section A.4, p. 205, Table A.1] |
| `threeDSSessionData` | 3DS Requestor | Optional opaque Browser form value | If supplied with the CReq, the ACS returns it unchanged with the final CRes POST; it is not a protocol transaction ID | **Normative Requirement / Interpretation.** [EMV 3DS v2.2.0, section A.5.4, p. 226, Table A.3] |

**Normative Requirement.** For App challenge preparation, the ACS stores `sdkTransID` for CReq and `threeDSServerTransID` plus `dsTransID` for RReq. For Browser challenge preparation it stores the Server Transaction ID, DS URL, and DS Transaction ID. For Decoupled it stores the Server and DS Transaction IDs for later RReq in the applicable App, Browser, or 3RI branch. [EMV 3DS v2.2.0, section 3.1, p. 50, Reqs 32 and 321; section 3.3, pp. 64-65, Reqs 109 and 325-326; section 3.4, p. 75, Req 328]

**Interpretation.** The identifiers have different creators and are not aliases. The source does not authorize deriving one from another or replacing any of them with an issuer order, account, or authentication-service identifier. [EMV 3DS v2.2.0, section A.4, pp. 157, 164, 198, and 214, Table A.1]

### 3.2 Recommended database and state model

**Issuer ACS Implementation Consideration — non-normative reference design.** Use an internal, non-protocol `acs_transaction_key` and maintain separately indexed columns for all received protocol IDs. Recommended controls:

- unique constraints scoped to the applicable protocol identity, plus explicit version and device-channel fields;
- an append-only inbound/outbound message journal containing hashes or redacted metadata, never sensitive verification values;
- explicit state/version fields and compare-and-set terminal transitions;
- persisted deadlines for initial challenge, continuation, Browser interface, SDK maximum, and Decoupled timing;
- a result-notification outbox that represents one **logical** RReq while recording each transport delivery attempt;
- a correlation table for internal fraud, OTP, OOB, and Decoupled jobs using issuer-generated IDs mapped to—not substituted for—the four protocol transaction IDs;
- retention and deletion rules set by applicable issuer, scheme, PCI, privacy, and legal policy.

The source requires the named protocol IDs and selected stored values but does not prescribe this schema, database technology, transaction isolation, retention period, replay cache, or deletion policy. [EMV 3DS v2.2.0, section 3.1, p. 50, Req 32; section 3.3, pp. 64-65, Req 109; section 3.4, p. 75, Req 328; section A.4, pp. 157, 164, 198, and 214, Table A.1]

### 3.3 Idempotency and duplicate handling

**Normative Requirement.** The source defines Error Code `204` for a duplicate data element, `301` for an invalid or duplicate Transaction ID, and `305` for a CReq sent to the wrong ACS, inconsistent with ARes, or using an ACS Transaction ID already received and processed. [EMV 3DS v2.2.0, section 5.4, p. 115, Req 218; section A.5.5, pp. 228-229, Table A.4]

**Interpretation / Unresolved.** Sections 5.1.6 and 5.9 do not define a universal duplicate-message key, cached-response replay rule, or uncertain-delivery recovery algorithm, and do not reconcile every `204`/`301`/`305` condition with a receiver-specific route. [EMV 3DS v2.2.0, sections 5.1.6 and 5.9, pp. 114 and 124-132; section A.5.5, pp. 228-229, Table A.4] See [`unresolved-questions.md`](unresolved-questions.md).

**Issuer ACS Implementation Consideration — non-normative reference design.** Make protocol transitions idempotent by `(messageType, protocol IDs, phase, logical sequence/counter)` and preserve the first committed terminal outcome. Retransmit the same logical RReq on connection retry instead of creating a second semantic result. Whether an exact duplicate receives a cached response, an Erro, or a scheme-defined recovery response remains subject to the unresolved `204`/`301`/`305` routing questions in [`unresolved-questions.md`](unresolved-questions.md).

## 4. Common message processing

### 4.1 Transport and serialization

**Normative Requirement plus example-backed Method-completion interpretation.** Server messages are HTTP POST request/response exchanges over the secured links in Chapter 6, using HTTP/1.1 or later and UTF-8 JSON. App challenge messages use JOSE media type and JWE; Browser challenge form carriage uses URL-encoded forms and HTML responses. Browser `creq`/`cres` and the initial Method form value are explicitly Base64url encoded. Req 263 does not restate JSON/Base64url construction for the Method completion value; Annex A.5.3 Example 2 demonstrates it. [EMV 3DS v2.2.0, sections 5.1.1-5.1.3, pp. 111-112, Reqs 184-193 and 313; section 5.7.1, p. 121, Reqs 253-254; sections 5.8.1-5.8.2, pp. 122-123, Reqs 255-270; sections A.5.3-A.5.4, pp. 224-226, Tables A.2-A.3]

**Interpretation.** Base64url is reversible encoding and does not itself provide encryption, integrity, peer authentication, or replay protection. The actual security layers are those separately specified for the applicable direct link and, for App CReq/CRes, JWE. [EMV 3DS v2.2.0, sections 5.1.2-5.1.3, p. 112, Reqs 191-193; sections 6.1.4 and 6.2.4, pp. 136-144]

**Normative Requirement.** An ACS accepts AReq, CReq, RRes, and Erro as inbound protocol message types. A receiver validates message type, version, required/conditional fields, field edits, message-specific content, matching IDs/reference numbers, and critical extensions. For otherwise valid unexpected non-extension fields, Req 209 permits either immediate ignore/drop or format validation followed by ignore/drop when valid and an Error Message when invalid; unrecognised non-critical extension pairs follow Req 210's separate silent-ignore/pass rule. [EMV 3DS v2.2.0, sections 5.1.4-5.1.6, pp. 112-114, Reqs 194-210, 212-213, and 320; sections 5.9.2, 5.9.5-5.9.6, and 5.9.11-5.9.12, pp. 124-132; sections A.4 and A.6-A.6.3, pp. 148-222 and 234-237, Tables A.1 and A.7]

### 4.2 AReq/ARes

**Normative Requirement plus unresolved status handling.** On a valid AReq, the ACS creates `acsTransID`, checks whether authentication is available, evaluates the request, selects an ARes Transaction Status, formats ARes, and returns it to the DS. A non-`C`/non-`D` ARes ends ACS AReq processing for that branch; `C` prepares challenge and `D` prepares Decoupled processing. For App/Browser, downstream 3DS Server handling of permitted status `I` is not stated. [EMV 3DS v2.2.0, section 3.1, pp. 48-52, Reqs 25-40, 318, 321, 323, and 386; section 3.3, pp. 63-66, Reqs 103-118 and 325-327; section 3.4, pp. 74-76, Reqs 287-299 and 328]

**Scheme/Vendor/External Dependency.** ECI, Authentication Value, Attempts support, certain Transaction Status/Reason combinations, and DS substitution of ARes on error are not completely determined by the Core specification. [EMV 3DS v2.2.0, section 3.1, pp. 49-51, Reqs 31 and 35; section 3.4, pp. 74-75, Reqs 291-292 and source footnote 6; sections 5.9.3.1-5.9.3.2, pp. 125-126]

### 4.3 CReq/CRes

**Normative Requirement plus Unresolved Browser carriage.** In App, every CReq and CRes is a direct protected SDK-ACS exchange and several pairs may occur. In Browser, the Requestor posts one protocol CReq through the Browser to the ACS; subsequent Cardholder interactions are ACS HTML/form exchanges, followed by a final Browser-carried CRes after RReq/RRes. The Browser final-CRes field-set, Content-Type, and return-link conflicts are not normalized here. [EMV 3DS v2.2.0, section 3.1, pp. 52-56, Reqs 45-61 and 76-78; section 3.3, pp. 66-70, Reqs 117-123 and 138-140; section 5.7.1, p. 121, Reqs 253-254; section 5.8.2, p. 123, Reqs 266-270; section A.5.4, p. 226, Table A.3] See [`unresolved-questions.md`](unresolved-questions.md).

### 4.4 RReq/RRes

**Normative Requirement.** After an App or Browser challenge reaches an outcome, the ACS sends one RReq through the DS to the 3DS Server; the acknowledgement returns as RRes through the DS. For every ARes `D`, the ACS sends exactly one semantic RReq, immediately when a successful or unsuccessful result is available or, without a result, on the Decoupled timeout path. [EMV 3DS v2.2.0, section 3.1, pp. 54-56, Reqs 62-75, 345-346, especially Reqs 66 and 345; section 3.3, pp. 68-70, Reqs 124-137 and 347-348, especially Reqs 128 and 347; section 3.4, pp. 76-77, Reqs 332-341 and 349-353]

**Interpretation.** `RRes` acknowledges result delivery; it is not a second authentication decision. The source defines `resultsStatus` for RRes and requires the ACS to validate/log the response or Erro. [EMV 3DS v2.2.0, sections 3.1, 3.3, and 3.4, pp. 55-56, 69-70, and 77, Reqs 72-75, 134-137, and 338-341; section A.4, p. 211, Table A.1; section B.9, p. 269, Table B.9]

### 4.5 3DS Method

**Normative Requirement plus normative recommendation and interpretation.** Where cached Card Range Data contains a 3DS Method URL, the Requestor invokes the Method before sending AReq for the same transaction. Req 260 explicitly makes the initial hidden-iframe value Base64url JSON containing the same `threeDSServerTransID` and a Method Notification URL; the flow overview also permits connection to an entity designated by the ACS. The ACS stores applicable Browser values under that ID and returns the recalled ID, through the Browser, to the Notification URL. Req 263 does not repeat JSON/Base64url construction for that completion value; Annex A.5.3 Example 2 demonstrates it. Completion within 10 seconds produces Method Completion Indicator `Y`; otherwise the 3DS Server uses `N`, while absence of a Method URL produces `U`. Req 264 says the ACS **should** ensure its Method actions do not affect the user experience. How a designated entity gathers, transfers, or exposes the values to the ACS is outside scope. [EMV 3DS v2.2.0, section 3.3, p. 60, Reqs 83-85; section 5.8.1, pp. 122-123, Reqs 255-264 and 315; section 6.1.8, p. 137; sections A.5.3 and A.5.7, pp. 224-225 and 232-233, Tables A.2 and A.6]

**Unresolved.** The completion-value JSON/Base64url construction is example-backed but not repeated in Req 263; the exact start event for the 10-second Method timer and treatment of a late callback are also not defined. See [`unresolved-questions.md`](unresolved-questions.md).

## 5. Flow state machines

The state names below are **Issuer ACS Implementation Considerations** used to make the normative transitions implementable. Only the cited transition conditions and outputs are protocol requirements.

### 5.1 App frictionless and native challenge

| Reference state | Event and required ACS action | Next state/output | Classification and source |
|---|---|---|---|
| `APP_AREQ_RECEIVED` | Validate AReq, create `acsTransID`, check availability, and evaluate disposition | `APP_FINAL_ARES`, `APP_CHALLENGE_PREPARED`, or `APP_DECOUPLED_PENDING` | **Normative Requirement.** [EMV 3DS v2.2.0, section 3.1, pp. 48-50, Reqs 25-32, 318, 321, and 386] |
| `APP_FINAL_ARES` | Return ARes with `Y`, `A`, `N`, `U`, `R`, or `I` disposition as applicable; do not initiate Cardholder interaction on a non-challenge branch | ARes propagation completes the ACS-side response; later 3DS Server handling for `I` is not stated, unlike the other enumerated statuses | **Normative Requirement / Unresolved `I` downstream handling.** [EMV 3DS v2.2.0, section 3.1, pp. 49 and 51-52, Reqs 30 and 35-41 and 323] [Related unresolved question](unresolved-questions.md#source-ambiguity) |
| `APP_CHALLENGE_PREPARED` | Set `transStatus=C`; choose an acceptable method; set rendering; establish secure-channel material; store SDK, Server, and DS correlation values; return ARes | `WAIT_INITIAL_APP_CREQ` | **Normative Requirement.** [EMV 3DS v2.2.0, section 3.1, pp. 50-52, Reqs 32-46] |
| `WAIT_INITIAL_APP_CREQ` | Receive and validate initial JWE CReq; set Interaction Counter `0` and Challenge Completion Indicator `N`; obtain UI/method data | `APP_WAIT_INPUT` after CRes | **Normative Requirement.** [EMV 3DS v2.2.0, section 3.1, p. 53, Reqs 47-55] |
| `APP_WAIT_INPUT` | Receive next CReq and validate/check Cardholder-entered authentication data | success, repeat, cancellation, or terminal failure | **Normative Requirement.** [EMV 3DS v2.2.0, section 3.1, pp. 53-55, Reqs 56-61, 310] |
| `APP_WAIT_INPUT` -> repeat | On incorrect input increment Interaction Counter; below the ACS maximum, prepare new challenge information and send another CRes | `APP_WAIT_INPUT` | **Normative Requirement.** [EMV 3DS v2.2.0, section 3.1, p. 54, Req 61] |
| `APP_WAIT_INPUT` -> success | On correct input increment the counter, set `Y`, applicable DS ECI/Authentication Value, and Challenge Completion Indicator `Y` | `APP_RESULT_NOTIFY` | **Normative Requirement.** [EMV 3DS v2.2.0, section 3.1, p. 54, Reqs 60-61] |
| `APP_WAIT_INPUT` -> max failure | At or above the ACS maximum set `N`, reason `19`, applicable DS ECI, and completion `Y` | `APP_RESULT_NOTIFY` | **Normative Requirement.** [EMV 3DS v2.2.0, section 3.1, p. 54, Req 61] |
| `APP_WAIT_INPUT` -> cancellation | Process the Challenge Cancelation Indicator/abandonment and carry the appropriate cancellation data into RReq | `APP_RESULT_NOTIFY` | **Normative Requirement.** [EMV 3DS v2.2.0, section 3.1, pp. 54-55, Reqs 59-60, 64, and 310] |
| `APP_RESULT_NOTIFY` | Send RReq through DS; receive, validate, and log a valid RRes | `APP_FINAL_CRES` | **Normative Requirement.** [EMV 3DS v2.2.0, section 3.1, pp. 54-56, Reqs 62-75] |
| `APP_RESULT_NOTIFY` error branch | On invalid RRes, return Erro to the DS. For an identifiable non-Decoupled transaction, send the applicable Erro to the SDK after either invalid RRes or inbound DS Erro; suppress that SDK branch for Decoupled. Do not enter normal final-CRes processing | App error terminal | **Unnumbered Normative Processing Direction.** [EMV 3DS v2.2.0, section 5.9.11, pp. 130-131] |
| `APP_FINAL_CRES` | Format, protect, and send final CRes to SDK; preserve Req 76's Table B.5 requirement and its conflicting Section 6.2.4.4 reference to Table B.4 | terminal, subject to field-set clarification | **Normative Requirement / Unresolved field-set cross-reference.** [EMV 3DS v2.2.0, section 3.1, p. 56, Reqs 76-79; section 6.2.4.4, p. 144; sections B.4-B.5, pp. 265-266, Tables B.4-B.5] [Related unresolved question](unresolved-questions.md#source-ambiguity) |

### 5.2 App OOB challenge

| Reference state | Event and required behavior | Next state/output | Classification and source |
|---|---|---|---|
| `OOB_PREPARED` | Use the normal `C` ARes and App challenge setup; SDK sends initial CReq and ACS returns CRes containing instructions | `OOB_EXTERNAL_PENDING` | **Interpretation applying normative challenge requirements.** [EMV 3DS v2.2.0, section 3.1, pp. 50-53, Reqs 32 and 42-55; section 3.2, p. 57, Figure 3.2] |
| `OOB_EXTERNAL_PENDING` | Cardholder authenticates to the ACS or Issuer/service provider outside the defined SDK input exchange | `OOB_RESULT_AVAILABLE` or repeat instructions | **Interpretation / Scheme/Vendor/External Dependency.** [EMV 3DS v2.2.0, section 3.2, pp. 57-58, Figure 3.2] |
| `OOB_EXTERNAL_PENDING` | On same-device return to foreground, SDK automatically posts CReq; that CReq acknowledges possible completion but does not contain the OOB result | remain pending until ACS obtains the issuer-side result | **Interpretation.** [EMV 3DS v2.2.0, section 3.2, pp. 57-58] |
| `OOB_EXTERNAL_PENDING` | If authentication has not occurred, ACS may return another CRes with instructions and repeat the CRes/CReq interaction | `OOB_EXTERNAL_PENDING` | **Interpretation applying challenge loop.** [EMV 3DS v2.2.0, section 3.2, p. 58; section 3.1, p. 54, Req 61] |
| `OOB_RESULT_AVAILABLE` | Convert the issuer-side outcome into the protocol result, send RReq, and after a valid RRes send final CRes; invalid RRes or inbound DS Erro follows the App error branch instead | normal terminal or App error terminal | **Interpretation applying normative result requirements.** [EMV 3DS v2.2.0, section 3.1, pp. 54-56, Reqs 62-79; section 3.2, pp. 57-58; section 5.9.11, pp. 130-131] |

**Unresolved.** A manual Continue control and same-device automatic foreground CReq apply to different OOB contexts; do not require both. See [`unresolved-questions.md`](unresolved-questions.md).

### 5.3 Browser frictionless and challenge

| Reference state | Event and required ACS action | Next state/output | Classification and source |
|---|---|---|---|
| `BRW_AREQ_RECEIVED` | Validate AReq, create `acsTransID`, evaluate disposition | final ARes, challenge, or Decoupled | **Normative Requirement.** [EMV 3DS v2.2.0, section 3.3, pp. 63-65, Reqs 103-109 and 325-326] |
| `BRW_FINAL_ARES` | Return the applicable non-`C`/non-`D` ARes; status `I` is permitted by Req 107 but omitted from the later Server-handling branches | terminal after defined ARes branches; downstream `I` handling unresolved | **Normative Requirement / Unresolved.** [EMV 3DS v2.2.0, section 3.3, pp. 64-66, Reqs 107 and 110-118 and 327] |
| `BRW_CHALLENGE_PREPARED` | Determine that an acceptable Browser challenge method is supported, set `C`, provide `acsURL`, and store Server Transaction ID, DS URL, and DS Transaction ID | `WAIT_BROWSER_CREQ` | **Normative Requirement.** [EMV 3DS v2.2.0, section 3.3, pp. 64-65, Req 109] |
| `WAIT_BROWSER_CREQ` | Receive the single Browser CReq, validate it, set counter `0`, and prepare/return challenge HTML | `BRW_WAIT_FORM` | **Normative Requirement.** [EMV 3DS v2.2.0, section 3.3, pp. 67-68, Reqs 119-123; section 5.8.2, p. 123, Reqs 267-269] |
| `BRW_WAIT_FORM` | Process one or more Browser HTML/form interactions; success, failure below maximum, maximum failure, or abandonment determines the result | repeat or `BRW_RESULT_NOTIFY` | **Normative Requirement.** [EMV 3DS v2.2.0, section 3.3, pp. 67-68, Reqs 122-126 and 307] |
| `BRW_RESULT_NOTIFY` | Send RReq; receive, validate, and log a valid RRes | `BRW_FINAL_CRES` | **Normative Requirement.** [EMV 3DS v2.2.0, section 3.3, pp. 68-70, Reqs 124-137] |
| `BRW_RESULT_NOTIFY` error branch | On invalid RRes, return Erro to the DS. For an identifiable non-Decoupled transaction, produce the Section 5.9.12 Browser failure CRes after either invalid RRes or inbound DS Erro; suppress it for Decoupled. The source does not restate its content/status fields | Browser failure-CRes terminal or Decoupled terminal | **Unnumbered Normative Processing Direction / Unresolved failure content.** [EMV 3DS v2.2.0, section 5.9.12, pp. 131-132] [Related unresolved question](unresolved-questions.md#source-ambiguity) |
| `BRW_FINAL_CRES` | Format final CRes, Base64url encode it, and invoke the Browser to POST it to the supplied Notification URL; preserve the source's field-set, Content-Type, return-link, and terminating-component conflicts | terminal | **Normative Requirement / Unresolved.** [EMV 3DS v2.2.0, section 3.3, p. 70, Reqs 138-140; section 5.1.2, p. 112, Req 191; sections 5.9.6 and 5.9.12, pp. 127 and 131-132, source footnote 7; section A.4, p. 206, Table A.1; section A.5.4, p. 226, Table A.3] [Related unresolved questions](unresolved-questions.md#source-ambiguity) |

### 5.4 Decoupled authentication for App, Browser, and 3RI

| Reference state | Event and required ACS action | Next state/output | Classification and source |
|---|---|---|---|
| `DECOUPLED_ACCEPTED` | Determine that an acceptable method is supported based in part on Requestor Decoupled Max Time; return ARes `D`, set ACS Decoupled Confirmation Indicator `Y`, and store Server/DS IDs | `DECOUPLED_PENDING` | **Normative Requirement.** [EMV 3DS v2.2.0, section 3.1, p. 50, Req 321; section 3.3, p. 65, Reqs 325-326; section 3.4, p. 75, Req 328] |
| `DECOUPLED_PENDING` | Start the Max Time timer and authenticate the Cardholder outside the EMV 3DS message path; no CReq/CRes occurs | result available or timeout | **Normative Requirement / Scheme/Vendor/External Dependency.** [EMV 3DS v2.2.0, section 2.7, p. 42; section 3.1, pp. 44, 50, and 55, Decoupled note and Reqs 321-322 and 345-346; section 3.3, pp. 59, 65, and 69, Figure 3.3 note and Reqs 325-326 and 347-348; section 3.4, pp. 71 and 75-77, Figure 3.4, Reqs 328, 330, and 349-354] |
| `DECOUPLED_PENDING` -> result | Send exactly one RReq immediately after a successful or unsuccessful result | `DECOUPLED_WAIT_RRES` | **Normative Requirement.** [EMV 3DS v2.2.0, section 3.1, p. 55, Reqs 66 and 345; section 3.3, pp. 68-69, Reqs 128 and 347; section 3.4, p. 76, Reqs 349-353] |
| `DECOUPLED_PENDING` -> no result | On Max Time expiry send one RReq without an authentication result, subject to the printed one-hour grace wording | `DECOUPLED_WAIT_RRES` | **Normative Requirement / Unresolved.** [EMV 3DS v2.2.0, section 3.1, p. 55, Req 345; section 3.3, p. 69, Req 347; section 3.4, p. 76, Reqs 352-353] |
| `DECOUPLED_WAIT_RRES` | Receive, validate, and log RRes or Erro; do not send a client final CRes for Decoupled | terminal | **Normative Requirement / Flow Boundary.** [EMV 3DS v2.2.0, section 2.7, p. 42; section 3.1, p. 44, Decoupled note; section 3.3, p. 59, Figure 3.3 note; section 3.4, p. 77, Reqs 338-341; sections 5.9.11-5.9.12, pp. 130-132] |

**Unresolved.** The one-hour ACS grace in Req 353 and the 3DS Server minimum wait of Max Time plus 30 seconds in Req 354 require an external retention/interoperability decision. The treatment of invalid/Erro RRes after Decoupled processing is also incomplete. [EMV 3DS v2.2.0, section 3.4, pp. 76-77, Reqs 353-354; sections 5.9.11-5.9.12, pp. 130-132] See [`unresolved-questions.md`](unresolved-questions.md).

### 5.5 3RI

| Reference state | Event and required ACS action | Next state/output | Classification and source |
|---|---|---|---|
| `3RI_AREQ_RECEIVED` | Validate AReq, create `acsTransID`, assess authentication availability and disposition using applicable 3RI/Decoupled inputs | final ARes or `D` | **Normative Requirement.** [EMV 3DS v2.2.0, section 3.4, pp. 74-75, Reqs 287-294 and 328] |
| `3RI_FINAL_ARES` | Return `Y`, `A`, `N`, `U`, `R`, or `I` as applicable; no 3DS Client/Cardholder challenge lane exists in the 3RI flow | terminal after ARes reaches Requestor | **Normative Requirement / Interpretation.** [EMV 3DS v2.2.0, section 3.4, pp. 71 and 74-76, Figure 3.4, Reqs 291-299] |
| `3RI_DECOUPLED_PENDING` | Follow the common Decoupled state machine, using an external Authentication Service interaction outside the protocol | RReq/RRes then terminal | **Normative Requirement / Interpretation.** [EMV 3DS v2.2.0, section 3.4, pp. 71 and 75-77, Figure 3.4, Reqs 328, 330, 332-341, and 349-354] |

**Unresolved.** Req 289's immediate-recipient wording must not be used to bypass the DS; Req 357's “website” instruction conflicts with the broader 3RI UI text; Attempts and result outputs remain DS/Payment System-dependent. [EMV 3DS v2.2.0, section 3.4, pp. 71, 74-76, Figure 3.4, Reqs 289-292 and 357] See [`unresolved-questions.md`](unresolved-questions.md).

## 6. Challenge preparation versus challenge execution

| Phase | Protocol behavior | What has not happened yet / what follows | Classification and source |
|---|---|---|---|
| App preparation during AReq disposition | ACS selects `C`, chooses an acceptable method, sets App rendering and secure-channel setup data, and persists App correlation/routing state before returning ARes | No Cardholder challenge input has yet been entered or verified | **Normative Requirement / Interpretation.** [EMV 3DS v2.2.0, section 3.1, pp. 50-52, Reqs 32-44] |
| Browser preparation during AReq disposition | ACS selects `C`, determines that an acceptable Browser challenge method is supported, sets `acsURL`, and stores Server Transaction ID, DS URL, and DS Transaction ID | Browser challenge HTML and method-specific UI content are prepared only after the later Browser CReq; no Cardholder challenge input has yet been entered or verified | **Normative Requirement / Interpretation.** [EMV 3DS v2.2.0, section 3.3, pp. 64-68, Reqs 109 and 119-123] |
| Execution start — App | Initial CReq is validated; counter becomes `0`; completion becomes `N`; ACS obtains the selected UI/method data and sends CRes | Cardholder input occurs only after SDK renders the CRes UI | **Normative Requirement.** [EMV 3DS v2.2.0, section 3.1, p. 53, Reqs 47-58] |
| Execution start — Browser | Browser CReq is validated; counter becomes `0`; ACS returns HTML and begins Browser form interactions | Subsequent form exchanges are not additional protocol CReq/CRes pairs | **Normative Requirement / Interpretation.** [EMV 3DS v2.2.0, section 3.3, pp. 67-68, Reqs 119-123; section 5.8.2, p. 123, Reqs 267-269] |
| Normal execution completion or Cardholder cancellation | ACS commits the challenge outcome, sends RReq and receives RRes, then sends final CRes for the non-Decoupled App or Browser challenge; Browser final-CRes carriage remains subject to the recorded source conflicts | Result notification precedes normal final client completion | **Normative Requirement / Unresolved for Browser final carriage.** [EMV 3DS v2.2.0, section 3.1, pp. 54-56, Reqs 59-79 and 310; section 3.3, pp. 68-70, Reqs 124-140; section A.5.4, p. 226, Table A.3] [Related unresolved questions](unresolved-questions.md#source-ambiguity) |
| Protocol timeout completion | Initial/App-continuation ACS timeout sends the specified RReq and clears applicable ephemeral key material but does not prescribe final App CRes; Browser-interface timeout sends both RReq `N` and Browser CRes `N` | Timeout behavior is branch-specific, not the normal final-CRes sequence | **Normative Requirement / Interpretation (evidence boundary).** [EMV 3DS v2.2.0, section 5.5.1, pp. 116-117, Reqs 219-227 and 343-344] |

## 7. OTP and other verification-method lifecycle

The detailed separation of normative behavior, reference architecture, optional issuer APIs/events, and policy decisions is in [`verification-and-otp-design.md`](verification-and-otp-design.md).

| Lifecycle phase | EMV 3DS boundary | Issuer-side boundary | Classification and source |
|---|---|---|---|
| Selection | ACS chooses an acceptable challenge method using supported capability data during challenge preparation | Issuer policy may rank OTP, knowledge, OOB, push, or other available methods | **Normative Requirement for selection; Issuer ACS Implementation Consideration for ranking.** [EMV 3DS v2.2.0, section 3.1, p. 50, Req 32; section 3.3, pp. 64-65, Req 109] |
| Generation | No EMV 3DS message generates a passcode | OTP creation, entropy, hashing, storage, and association are internal issuer/vendor operations | **Interpretation / Scheme/Vendor/External Dependency.** [EMV 3DS v2.2.0, section 3.1, pp. 50 and 53-54, Reqs 32 and 50-61; section 3.2, pp. 57-58] |
| Delivery | CRes or Browser HTML carries ACS-defined challenge instructions/UI content; App figures illustrate, but do not mandate, masked phone/email examples | SMS/email/push/in-app delivery and provider callbacks are internal operations; destination masking is issuer/external policy | **Normative Requirement for UI; Interpretation / Issuer Policy Decision for delivery and masking.** [EMV 3DS v2.2.0, section 3.1, p. 53, Reqs 51-55; section 4.2.3, pp. 88-90, Figures 4.8-4.10; sections A.4 and A.8, pp. 192-222 and 257-258, Table A.18] |
| Entry | App Cardholder input is returned in a later CReq; Browser input is returned in an ACS HTML/form interaction | Input validation, normalization, and sensitive-field handling need issuer/web/SDK controls | **Normative Requirement / Issuer ACS Implementation Consideration.** [EMV 3DS v2.2.0, section 3.1, pp. 53-54, Reqs 56-60; section 3.3, pp. 67-68, Reqs 122-123 and 307; section A.7.7, p. 252, Table A.14] |
| Verification | ACS checks the Cardholder-entered authentication data; OOB and Decoupled outcomes arrive from authentication outside the message path | OTP compare, credential service call, device approval, and OOB result verification are internal issuer/vendor operations | **Normative Requirement / Interpretation.** [EMV 3DS v2.2.0, section 3.1, p. 54, Reqs 60-61; section 3.2, pp. 57-58; section 3.4, p. 76, Req 330] |
| Retry/resend | Incorrect App or Browser data follows the respective repeat/maximum logic. App Native UI has specified `resendChallenge`/label fields; the Core source defines no Browser resend field or form schema | Numeric maximum, resend rate, code rotation/reuse, delivery retries, Browser-private resend behavior, and channel fallback are issuer/external decisions | **Normative Requirement / Issuer Policy Decision.** [EMV 3DS v2.2.0, section 3.1, p. 54, Req 61; section 3.3, p. 68, Req 123; section 4.2.4, p. 95, Req 158; section A.4, pp. 210-211, Table A.1; section A.8, p. 257, Table A.18] |
| Expiry | Protocol challenge timers produce specified timeout results/cancellation values; SDK has its separate maximum timeout | OTP lifetime itself is not specified and need not equal the protocol timers | **Normative Requirement / Interpretation.** [EMV 3DS v2.2.0, sections 5.5.1-5.5.2.2, pp. 116-118, Reqs 219-227, 237, 239, 312, and 343-344] |
| Final status | For normally completed non-Decoupled App/Browser challenge, ACS maps the outcome to Transaction Status/reason/cancel data, completes RReq/RRes, and sends final CRes; App ACS timeout and Browser timeout instead follow their distinct Section 5.5.1 branches | Issuer audit and notification services consume the committed result but do not create additional EMV 3DS messages | **Normative Requirement / Issuer ACS Implementation Consideration.** [EMV 3DS v2.2.0, section 3.1, pp. 54-56, Reqs 61-79; section 3.3, pp. 68-70, Reqs 124-140; section 5.5.1, pp. 116-117, Reqs 219-227 and 343-344] |

### 7.1 Protocol messages versus issuer operations

| Activity | EMV 3DS protocol message? | Correct boundary |
|---|---:|---|
| AReq/ARes challenge decision and challenge setup | Yes | 3DS Server -> DS -> ACS and response |
| CReq/CRes or Browser challenge HTML/form | Yes for CReq/CRes; HTML/form is the specified Browser challenge transport | SDK/Browser <-> ACS |
| OTP code generation, hashing/storage, delivery-provider call, delivery receipt, resend throttling, code comparison, or credential lockout | No | Internal issuer/ACS/vendor service operation; this does not negate the ACS's normative selection of an acceptable EMV 3DS challenge method |
| OOB push initiation or Issuer/service-provider result callback | No | Internal issuer/vendor integration |
| Decoupled Authentication Service request/result | No | Dashed, outside-protocol interaction |
| RReq/RRes result notification | Yes | ACS -> DS -> 3DS Server and acknowledgement back |
| Fraud score/model call | No | Internal issuer/ACS integration; AReq supplies optional risk inputs but no risk API |

The “No” classifications are **Interpretations** bounded by the protocol's defined message set and explicit outside-scope statements. [EMV 3DS v2.2.0, sections 3.1-3.4, pp. 43-77; sections 5.1.1 and 5.8, pp. 111 and 122-123, Req 184; section 3.4, p. 76, Req 330; section A.7, p. 237]

## 8. Browser session and HTTP connection behavior

**Normative Requirement plus Unresolved final carriage.** Browser challenge begins with an HTTP POST carrying CReq to `acsURL`; the ACS returns HTML, several Browser interactions may follow, and the final Base64url CRes is posted through the Browser to the Notification URL. TLS server authentication protects the Browser-ACS challenge link; the final-return link/field-set/Content-Type conflicts remain unresolved below. [EMV 3DS v2.2.0, section 3.3, pp. 66-70, Reqs 117-140; section 5.1.2, p. 112, Req 191; section 5.8.2, p. 123, Reqs 266-270; section 6.1.4.2, p. 137; section A.5.4, p. 226, Table A.3]

**Interpretation.** These are separate HTTP requests and responses. The source does not require one HTTP request, TCP connection, or TLS connection to remain continuously open for the challenge. “Use the secure link” defines protected party-to-party communication, not mandatory socket reuse. A reverse proxy, load balancer, Browser, or server may close and establish connections between exchanges so long as protocol state and security requirements remain satisfied. [EMV 3DS v2.2.0, sections 2.4.3-2.4.4, pp. 34-35; section 3.3, pp. 66-70, Reqs 117-140; section 6.1.1, p. 134]

**Issuer ACS Implementation Consideration — non-normative reference design.** Correlate Browser requests with protocol IDs and an opaque server-side challenge handle. If `threeDSSessionData` is supplied, preserve it unchanged for final return; do not use it as a substitute for protocol IDs. Define cookie, SameSite, origin, CSP, storage, CSRF, form replay, late submission, load-balancer affinity, and session-expiry behavior under issuer/web-security policy because the Core specification does not define those controls. [EMV 3DS v2.2.0, section A.5.4, p. 226, Table A.3; section 3.3, pp. 66-70, Reqs 117-140]

**Unresolved.** Separate-request handling does not resolve the source's final-return link wording, Requestor/3DS Server Notification-URL termination, Browser Table B.4/B.5 field-set mismatch, App final-CRes B.5/B.4 Function K cross-reference, final-CRes Content-Type/form-carriage conflict, or late Browser submission behavior. These remain in [`unresolved-questions.md`](unresolved-questions.md).

## 9. Timers, retries, and availability-driven behavior

### 9.1 Normative timer and retry register

| Context | Start/limit | Required expiry or retry action | Source |
|---|---|---|---|
| App AReq/ARes processing display | Entire AReq/ARes cycle; minimum 2 seconds | SDK creates the processing screen; Requestor App displays it for the full cycle with the specified processing-screen content and Cancel behavior | **Normative Requirement.** [EMV 3DS v2.2.0, section 4.2.1.1, pp. 83-84, Reqs 141-146 and 360] |
| App CReq/CRes processing display | Processing screen remains until CRes or timeout; second and later cycles have a minimum 1-second display (no 1-second minimum is stated for the initial cycle) | Use the Consumer Device OS default processing graphic under the Native/HTML rules and retain the screen until response or timeout | **Normative Requirement / Interpretation (evidence boundary for the initial-cycle omission).** [EMV 3DS v2.2.0, sections 4.2.1.1, 4.2.4.1, and 4.2.7.1, pp. 84, 95, and 101, Reqs 147-148, 151, 153, 159, and 361] |
| Browser Requestor AReq/ARes processing display | Entire AReq/ARes cycle; minimum 2 seconds | Requestor website displays the specified processing graphic/DS-logo screen and no other design element | **Normative Requirement.** [EMV 3DS v2.2.0, section 4.3.1.1, p. 102, Reqs 172-176] |
| ACS Browser challenge processing display | Each ACS challenge-processing screen; minimum 1 second | Display the specified processing graphic in the Challenge/Processing zone and no other design element before the challenge interface | **Normative Requirement.** [EMV 3DS v2.2.0, section 4.3.1.2, p. 103, Reqs 178-182] |
| ACS wait for initial App or Browser CReq | ACS starts when it sends ARes `C`; 30 seconds | On expiry send RReq `N`, reason `14`, Challenge Cancelation `05`, clear any ephemeral key generated and stored for the current CReq/CRes exchange; a late CReq receives Erro `A`/`402` | **Normative Requirement.** [EMV 3DS v2.2.0, section 5.5.1, p. 116, Reqs 219-222] |
| App continuation after a non-final CRes | 10 minutes / 600 seconds after each successfully sent non-final CRes | On expiry send RReq `N`, reason `14`, Challenge Cancelation `04`, clear any ephemeral key generated and stored for that CReq/CRes exchange; late CReq receives Erro `A`/`402` | **Normative Requirement.** [EMV 3DS v2.2.0, section 5.5.1, p. 116, Reqs 223-225] |
| Browser challenge interface | 10 minutes after each Browser interface is presented | On expiry send RReq `N`, reason `14`, and CRes `N` to the Notification URL; challenge completes | **Normative Requirement.** [EMV 3DS v2.2.0, section 5.5.1, pp. 116-117, Reqs 226-227 and 343-344] |
| 3DS Server -> DS AReq connection | After the first connection failure, make one further attempt, possibly using an alternate DS URL | Second failure: return error to Requestor and end processing | **Normative Requirement.** [EMV 3DS v2.2.0, section 5.5.2.1, p. 117, Reqs 228-229] |
| 3DS Server -> DS ARes read | Timer starts after TLS handshake and complete AReq send; duration DS-defined | Expiry closes the connection and results in a failed 3DS transaction | **Normative Requirement / Scheme/Vendor/External Dependency.** [EMV 3DS v2.2.0, section 5.5.2.1, p. 117, Reqs 228, 231-232] |
| DS -> ACS AReq connection | After the first connection failure, either retry immediately or use an alternate ACS URL if available | Second failure: Erro `D`/`405` or DS-defined ARes | **Normative Requirement / Scheme/Vendor/External Dependency.** [EMV 3DS v2.2.0, section 5.5.2.1, p. 117, Req 233] |
| DS -> ACS ARes read | Timer starts after TLS handshake and complete AReq send; duration DS-defined | Expiry: Erro `D`/`402` or DS-defined ARes | **Normative Requirement / Scheme/Vendor/External Dependency.** [EMV 3DS v2.2.0, section 5.5.2.1, pp. 117-118, Reqs 234-235] |
| SDK -> ACS CReq connection | One immediate retry after first failure | Second failure: report App error and end SDK challenge processing | **Normative Requirement.** [EMV 3DS v2.2.0, section 5.5.2.2, p. 118, Req 236] |
| SDK wait for each CRes | 10 seconds after the TLS handshake completes and the full CReq is sent | If CRes does not arrive, end 3DS processing, send Erro `C`/`402` to ACS, and report an error to the App | **Normative Requirement.** [EMV 3DS v2.2.0, section 5.5.2.2, p. 118, Reqs 237 and 239] |
| SDK maximum challenge time | Starts after the TLS handshake completes and the first CReq is sent; configured at least 5 minutes | If final CRes does not arrive, end 3DS processing, send Erro `C`/`402` to ACS, and report an error to the App | **Normative Requirement.** [EMV 3DS v2.2.0, section 5.5.2.2, p. 118, Req 312] |
| ACS -> DS RReq connection | Immediate retry after first failure | After second failure, wait 10 seconds and retry until delivery, subject to unresolved wording that says delivery “with `U`” | **Normative Requirement / Unresolved.** [EMV 3DS v2.2.0, section 5.5.2.3, p. 118, Req 240] |
| ACS wait for RRes from DS | Starts after TLS handshake completes and the full RReq is sent; 5 seconds | Send Erro `A`/`402` to DS | **Normative Requirement.** [EMV 3DS v2.2.0, section 5.5.2.3, p. 118, Reqs 241-242] |
| DS -> 3DS Server RReq connection | Immediate retry after first connection/TLS failure | Second failure: send Erro `D`/`405` to ACS | **Normative Requirement.** [EMV 3DS v2.2.0, section 5.5.2.3, p. 119, Req 243] |
| DS wait for RRes from 3DS Server | Starts after TLS handshake completes and the full RReq is sent; 3 seconds | Send Erro `D`/`402` to ACS | **Normative Requirement.** [EMV 3DS v2.2.0, section 5.5.2.3, p. 119, Reqs 244-245] |
| 3DS Method | 10 seconds for completion | Method Completion Indicator `Y` on timely notification, `N` otherwise; absence of a Method URL produces `U` | **Normative Requirement / Unresolved start event.** [EMV 3DS v2.2.0, section 5.8.1, pp. 122-123, Reqs 258 and 315] |
| Decoupled | Requestor Decoupled Max Time; Table A.1 accepts numeric values `1`-`10080` while printing a 5-character length without a padding rule | Immediate RReq on result; no-result RReq at expiry with printed one-hour grace; Server waits at least Max Time + 30 seconds | **Normative Requirement / Unresolved coordination and field padding.** [EMV 3DS v2.2.0, section 3.1, p. 55, Reqs 345-346; section 3.3, p. 69, Reqs 347-348; section 3.4, pp. 76-77, Reqs 330 and 349-354; section A.4, p. 153, Table A.1] [Related unresolved questions](unresolved-questions.md#source-ambiguity) |
| PReq/PRes preparation dependency | At least once per 24 hours and no more than once per hour, conditional on no errors found during PRes processing | First connection failure: immediate retry; after a second failure, end that transaction and retry at 60-second intervals within the 24-hour window until success; more than one Card Range Data request within an hour receives `D`/`103` | **Normative Requirement; ACS not on this path.** [EMV 3DS v2.2.0, section 5.6, pp. 119-120, Reqs 246, 248-249, and 303] |
| PRes Card Range Data error and full reload | When Card Range Data contains an error | 3DS Server resubmits PReq without Serial Number; a PRes without Serial Number replaces all existing Card Range Data for that DS | **Normative Requirement; ACS not on this path.** [EMV 3DS v2.2.0, section 5.6, pp. 120-121, Req 385] |

**Unresolved.** DS-defined AReq/ARes read durations, OOB timeout adjustment, the RReq `U` wording, late Browser form submissions, PRes read timeout, Method timer start/late callback, and Decoupled grace/wait coordination remain open in [`unresolved-questions.md`](unresolved-questions.md).

### 9.2 Partial outage

**Normative Requirement.** A component that detects a system failure and cannot continue processing stops accepting new requests and sends Error Code `403` or `404`, as applicable, for outstanding requests. [EMV 3DS v2.2.0, section 5.2, p. 115, Req 215]

**Unresolved.** The source does not tell the ACS how to choose between Error Code `403` and `404` in that outage branch. [EMV 3DS v2.2.0, section 5.2, p. 115, Req 215] [Related unresolved question](unresolved-questions.md#implementation-decision)

**Issuer ACS Implementation Consideration — non-normative reference design.** Route readiness should include the ability to accept, persist, time, and complete the message class being advertised as healthy. Drain or fail closed per route; avoid acknowledging AReq `C` unless challenge state and cryptographic material can be recovered by the serving cluster.

## 10. Error handling

### 10.1 Common rules

**Normative Requirement plus unnumbered Normative Processing Direction.** `Erro` includes message type/version and error code/component/description/detail, plus available protocol transaction IDs. Receiver-specific error responses and result fallbacks depend on which component detected the error, which message was being processed, and whether the transaction can be identified. Section 5.9 contains no local numbered requirements. [EMV 3DS v2.2.0, sections 5.1.6, 5.4, and 5.9, pp. 114-115 and 124-132, Reqs 212-213 and 216-218; sections A.4 and A.5.5, pp. 199 and 227-230, Tables A.1 and A.4; section B.10, p. 270, Table B.10]

| ACS processing point | Required protocol handling | Source |
|---|---|---|
| Invalid/unrecognised/missing AReq received from DS | Return Erro to the DS with the branch-defined error fields and end the invalid-message path | **Unnumbered Normative Processing Direction.** [EMV 3DS v2.2.0, section 5.9.2, pp. 124-125; section A.5.5, pp. 227-230, Table A.4] |
| Invalid identifiable App CReq | Return Erro to SDK; for the identifiable transaction send RReq `U` with Challenge Cancelation Indicator `06` through DS before applicable client completion | **Unnumbered Normative Processing Direction.** [EMV 3DS v2.2.0, section 5.9.5, pp. 126-127; section A.4, p. 189, Table A.1] |
| SDK-originated Erro received during an identifiable App challenge | Treat the SDK Erro as the challenge failure input and send RReq `U` with Challenge Cancelation Indicator `06` through DS | **Unnumbered Normative Processing Direction.** [EMV 3DS v2.2.0, sections 5.9.5-5.9.5.1, pp. 126-127; section A.4, p. 189, Table A.1] |
| Invalid identifiable Browser CReq | Where the Notification URL branch permits a response, return the specified error through the Browser and send RReq `U` with Challenge Cancelation Indicator `06`; an unrecognised message may not expose a usable return URL | **Unnumbered Normative Processing Direction / Unresolved conditions.** [EMV 3DS v2.2.0, section 5.9.6, pp. 127-128, source footnote 7; section A.4, p. 189, Table A.1] |
| Invalid App RRes or DS Erro received by ACS | For invalid RRes, return ACS Erro to DS and, for an identifiable non-Decoupled transaction, send the applicable Erro to SDK. When the inbound message is itself Erro, send a new SDK Erro with component `A` and code `403`; suppress SDK forwarding for Decoupled | **Unnumbered Normative Processing Direction.** [EMV 3DS v2.2.0, section 5.9.11, pp. 130-131] |
| Invalid Browser RRes or DS Erro received by ACS | For invalid RRes, return ACS Erro to DS; for an identifiable non-Decoupled transaction, send CRes through the Browser. Apply the same Browser CRes notification branch when the inbound message is Erro; suppress it for Decoupled | **Unnumbered Normative Processing Direction / Unresolved failure-CRes details.** [EMV 3DS v2.2.0, section 5.9.12, pp. 131-132] |
| Late App CReq after ACS timer | Return Erro with Error Component `A` and Error Code `402` | **Normative Requirement.** [EMV 3DS v2.2.0, section 5.5.1, p. 116, Reqs 222 and 225] |
| RRes read timeout | Send Erro `A`/`402` to DS | **Normative Requirement.** [EMV 3DS v2.2.0, section 5.5.2.3, p. 118, Req 242] |

**Unresolved.** The Core specification does not define general HTTP status codes, Retry-After behavior, cached replay, or universal recovery for Erro. Duplicate codes/routes, Browser Notification URL validity branches, failure-branch final CRes fields, Decoupled RRes failure completion, and partial-outage `403` versus `404` selection are tracked in [`unresolved-questions.md`](unresolved-questions.md).

## 11. Link security and payload visibility

The link-by-link control and ownership inventory is in [`security-matrix.md`](security-matrix.md).

| Direct link | Required transport/authentication | Message-level protection and visibility | Classification and source |
|---|---|---|---|
| Consumer App/Browser -> 3DS Requestor (link a) | A secured connection satisfying Payment System security requirements with at least Requestor server authentication for 3DS-specific actions | No additional Core JWE/JWS wrapper is specified; the endpoints and any TLS terminator can see application plaintext | **Normative Requirement / Interpretation / Scheme/Vendor/External Dependency.** [EMV 3DS v2.2.0, section 6.1.1, pp. 134-135; section 6.2.1, p. 138] |
| 3DS Requestor <-> 3DS Server when separate | Transferred data is protected to Payment System security requirements and both servers are mutually authenticated; the mechanism and certificate profile are not stated | No general Core message-level wrapper or private API is defined | **Normative Requirement / Scheme/Vendor/External Dependency.** [EMV 3DS v2.2.0, section 6.1.1, p. 135] |
| 3DS Server <-> DS (link b) | DS-CA-issued X.509 certificates and mutual TLS; Server is TLS client for AReq and DS is TLS client for RReq | Server JSON is protected in transit by TLS; the absence of a general JWE/JWS wrapper and endpoint plaintext visibility are interpretations of the stated transport profile | **Normative Requirement / Interpretation.** [EMV 3DS v2.2.0, section 6.1.2, p. 135; Annex D, p. 273] |
| 3DS Server -> DS PReq/PRes | Req 247 uses the section 6.1.2.1 authentication-direction secure link: DS-CA mutual TLS with the 3DS Server as client and DS as server | The absence of a PReq/PRes JWE/JWS wrapper and endpoint plaintext visibility are interpretations; the subsection's AReq/ARes-only wording remains unresolved | **Normative Requirement / Interpretation / Unresolved subsection wording.** [EMV 3DS v2.2.0, section 5.6, p. 119, Req 247; section 6.1.2.1, p. 135; Annex D, p. 273] |
| DS <-> ACS (link c) | DS-CA-issued X.509 certificates and mutual TLS; DS is TLS client for AReq and ACS is TLS client for RReq | AReq/ARes/RReq/RRes JSON is protected in transit by TLS; DS and ACS see plaintext | **Normative Requirement / Interpretation.** [EMV 3DS v2.2.0, section 6.1.3, p. 136; Annex D, p. 273] |
| SDK <-> ACS App challenge (link d) | SDK is TLS client; ACS presents a server certificate from a commercial CA | Signed ARes setup content establishes ephemeral keys; CReq/CRes is JWE-protected with direction counters/MAC; TLS and message-level protection both apply | **Normative Requirement.** [EMV 3DS v2.2.0, section 5.7.1, p. 121, Reqs 253-254; section 6.1.4.1, p. 136; sections 6.2.3-6.2.4, pp. 141-144; Annex D, p. 273] |
| Browser <-> ACS challenge (link d) | Browser is TLS client; ACS presents a commercial-CA server certificate; no client certificate is specified | Browser form/HTML payload is visible to Browser and ACS; Base64url carriage is not encryption | **Normative Requirement / Interpretation.** [EMV 3DS v2.2.0, section 6.1.4.2, p. 137; sections 5.1.2 and 5.8.2, pp. 112 and 123, Reqs 191-193 and 266-270] |
| Browser <-> ACS 3DS Method (link h) | Separate server-authenticated TLS link; Chapter 6 names an ACS commercial-CA server certificate, while the flow overview permits an ACS-designated Method entity whose integration is outside scope | `threeDSMethodData` is Base64url JSON visible to Browser and the terminating Method endpoint; it is not encrypted by Base64url | **Normative Requirement / Interpretation / External Dependency.** [EMV 3DS v2.2.0, section 3.3, p. 60, Reqs 83-85; section 5.8.1, pp. 122-123, Reqs 259-264; section 6.1.8, p. 137] |
| Browser -> supplied final-CRes Notification URL | Section 6.1.1 says a further secured Browser-Requestor link may be required when the return URL differs from the initiator endpoint; protocol-specific Requestor interaction is expected to satisfy Payment System security requirements with at least Requestor server authentication, but the exact return binding/profile is unresolved | Final CRes is carried in a Base64url form field; Browser and terminating endpoint can see the decoded CRes; Base64url supplies no confidentiality. The terminating Requestor/3DS Server component is unresolved | **Normative source boundary / Interpretation / Unresolved exact link and recipient wording.** [EMV 3DS v2.2.0, section 6.1.1, p. 134; section 3.3, p. 70, Reqs 138-140; sections 5.9.6 and 5.9.12, pp. 127 and 131-132, source footnote 7; section A.4, p. 206, Table A.1; section A.5.4, p. 226, Table A.3] |
| Browser -> 3DS Method Notification URL | Section 6.1.1 says a further secured Browser-Requestor link may be required when this return URL differs from the initiator endpoint; it does not unconditionally define a separate endpoint or its certificate profile | Browser and Notification endpoint can see the returned Method value. Annex A.5.3 Example 2 shows Base64url JSON, but Req 263 does not repeat Req 260's encoding rule for completion | **Normative source boundary / Interpretation / Unresolved completion wording / Scheme Dependency.** [EMV 3DS v2.2.0, section 6.1.1, p. 134; section 5.8.1, pp. 122-123, Reqs 260 and 263; section A.5.3, pp. 224-225, Table A.2] |
| Payment links e/f/g | The Core specification adds no 3DS security requirements to the regular Integrator-Acquirer, Acquirer-Payment System, or Payment System-Issuer links | Authentication, confidentiality, integrity, keys, certificates, and visibility are governed by the regular payment/Payment System profiles, not inferred here | **Normative source boundary / Scheme/Vendor/External Dependency.** [EMV 3DS v2.2.0, sections 6.1.5-6.1.7, p. 137] |
| ACS -> issuer OTP/OOB/Decoupled/risk/account services | No direct Core transport, client-authentication, certificate, or message-protection profile is defined | Visibility and controls depend on the issuer/vendor architecture; these links can carry sensitive verification data and outcomes | **Interpretation / Scheme/Vendor/External Dependency.** [EMV 3DS v2.2.0, section 3.2, pp. 57-58; section 3.4, pp. 71 and 76, Figure 3.4, Req 330] |

**Normative Requirement.** Annex D requires TLS 1.2 or later, RSA keys of at least 2048 bits or ECC keys of at least 256 bits, the stated TLS 1.2 ECDHE AES-GCM suite capabilities, use of P-256 and its indication in the printed “cipher suite extension,” and rejection of prohibited suites. [EMV 3DS v2.2.0, sections D.1-D.2, p. 273]

**Unresolved.** Annex D does not identify the printed “cipher suite extension” by TLS field or RFC; preserve the P-256 use-and-indication obligation and obtain the applicable interoperability profile. [EMV 3DS v2.2.0, section D.1.1, p. 273] See [`unresolved-questions.md`](unresolved-questions.md).

**Interpretation.** JWS signature protects integrity/authenticity but does not conceal its payload; JWE supplies message-level confidentiality and integrity for App challenge messages. Device Information is JWE-encrypted by the SDK for the DS, decrypted by the DS, and then included for ACS processing over the DS-ACS protected link. [EMV 3DS v2.2.0, sections 6.2.2-6.2.4, pp. 138-144]

**Issuer ACS Implementation Consideration — non-normative reference design.** Log message type, route, outcome, redacted IDs, latency, counter/timer state, error code, certificate identity, and policy version. Do not log OTPs, challenge data entry, authentication secrets, private/derived keys, decrypted SDK device payloads, full account data, or unrestricted AReq/CReq/CRes bodies. TLS terminators and observability agents can see TLS-only plaintext and therefore belong inside an explicitly controlled trust boundary.

**Normative Requirement plus Unresolved scope wording.** Req 247 binds PReq to the Section 6.1.2.1 3DS Server -> DS mutual-TLS link, even though that subsection itself names only AReq/ARes. Apply the explicit cross-reference and obtain the current DS profile rather than treating PReq/PRes as unprotected or inventing another link. [EMV 3DS v2.2.0, section 5.6, p. 119, Req 247; section 6.1.2.1, p. 135; Annex D, p. 273]

**Unresolved.** Obtain external profiles or clarification for the PReq/PRes subsection-scope wording, Browser/Annex D applicability, 3RI result-link wording, TLS 1.3+, certificate identity/roots/revocation/rotation, JOSE/KDF ambiguities, HSM/key custody, and final Browser CRes carriage. See [`unresolved-questions.md`](unresolved-questions.md).

## 12. Issuer implementation considerations

Everything in this section is **non-normative** unless a sentence includes a separate normative citation.

### 12.1 Availability

**Issuer ACS Implementation Consideration.** Deploy independent AReq, challenge/Method, result-notification, and internal verification capacity across failure domains. Replicate the minimum state needed to resume counters, timers, secure-channel context, and one-result delivery; use durable timer workers and result outboxes. A logical ACS may be partitioned, but the Core specification does not define the consistency mechanism. [EMV 3DS v2.2.0, sections 1.5 and 2.3.4, pp. 23 and 34; section 5.2, p. 115, Req 215]

### 12.2 Observability

**Issuer ACS Implementation Consideration.** Measure request/response latency by direct leg, DS-defined read timeout headroom, challenge state age, CReq/CRes or Browser interaction count, OTP/OOB provider latency, timer expiry, RReq transport attempts, RRes status, Erro code/component, certificate expiry, and outcome distribution. Keep the four protocol IDs separately searchable under access control; use tokenized or hashed operational dimensions where full values are unnecessary.

### 12.3 Audit

**Issuer ACS Implementation Consideration.** Maintain an append-only decision record containing source message reference, policy/model version, eligibility/risk decision, chosen method, displayed method metadata, internal verification correlation, attempt/counter changes, timer transitions, final status/reason/cancel value, logical RReq, delivery attempts, RRes/Erro acknowledgement, operator intervention, and data-retention action. DS logging requirements remain DS-specific. [EMV 3DS v2.2.0, sections 3.1, 3.3, and 3.4, pp. 51, 65, 69-70, 75, and 77, Reqs 36, 113, 136, 296, 308, 339, and 341]

### 12.4 Privacy

**Issuer ACS Implementation Consideration.** Minimize collection and retention; segregate Cardholder/account, device, risk, challenge-input, and authentication-service data; enforce need-to-know access; redact exports; and define deletion/legal-hold workflows. The Core specification's message fields do not themselves determine a lawful basis, PCI scope, retention period, or data-subject process.

### 12.5 Key and certificate lifecycle

**Issuer ACS Implementation Consideration.** Inventory DS-CA and commercial-CA trust anchors, server/client certificates, ACS signing and ephemeral keys, HSM ownership, deployment location, rotation overlap, revocation checking, expiry alarms, compromise response, backup/restore eligibility, and environment separation. Chapter 6 and Annex D establish link and algorithm requirements but do not define the complete lifecycle. [EMV 3DS v2.2.0, sections 6.1.2-6.1.4, 6.1.8, and 6.2.3, pp. 135-143; Annex D, p. 273]

### 12.6 Fraud and risk integration

**Issuer ACS Implementation Consideration.** Give the risk service a versioned, time-bounded input contract derived only from allowed AReq data and issuer-held data. Return a decision recommendation plus reason/model metadata, not a fabricated EMV status. The ACS remains the component that selects the protocol disposition. The Core specification describes four optional Requestor risk-information objects but does not define a fraud API, score, threshold, or model. [EMV 3DS v2.2.0, section 2.5.2, p. 36; section A.7, pp. 237-249, Tables A.8-A.11]

### 12.7 Disaster recovery

**Issuer ACS Implementation Consideration.** Set RTO/RPO by flow phase. Recovery must account for pending initial/continuation/Browser/Decoupled timers, App secure-channel counters and key state, committed outcomes, logical RReq outbox state, outstanding RRes waits, DS routing/certificates, and audit continuity. Test that restoration cannot issue a second semantic result or accept stale challenge input. The Core specification does not prescribe backup technology, region topology, RTO, or RPO.

## 13. Required external decisions and remaining gaps

All source conflicts and policy gaps are preserved in [`unresolved-questions.md`](unresolved-questions.md). At minimum, production approval requires the following evidence:

| Authority needed | Remaining decision or evidence |
|---|---|
| Current EMVCo material | Bulletins, errata, later clarifications, and any current applicability guidance for this December 2018 v2.2.0 source; especially version text, route/table cross-references, Browser final CRes, Method timing, duplicate handling, crypto wording, and Decoupled timing |
| Payment System / DS rules | DS read-timeout values; status/reason/Attempts/ECI/Authentication Value behavior; account-range onboarding; DS endpoints and certificates; message extensions; programme UI/logging; RReq retry interpretation; 3RI/MIT mapping; image/profile requirements |
| Certification and test programmes | Current ACS protocol, security, negative, duplicate, timeout, failover, Browser, App SDK interoperability, OOB, Decoupled, 3RI, and result-delivery test cases; the Core PDF does not enumerate a complete certification suite |
| PCI, privacy, regulatory, and legal | Applicable PCI requirements, authentication regulation, data classification, logging/redaction, retention/deletion, accessibility, consumer notice, incident response, vulnerability management, and lawful processing |
| Issuer policy | Risk thresholds; method eligibility; OTP generation/lifetime/attempt/resend/delivery/channel fallback; contact masking; lockout; OOB/Decoupled UX; late-result acceptance; manual operations; evidence retention |
| Vendor/platform decisions | Database/idempotency design; browser/session hardening; proxy/TLS termination; key/HSM lifecycle; cluster and DR mechanics; provider contracts; telemetry; SDK-specific behavior; certificate automation |

No item in this gap register may be silently replaced with general industry practice and presented as an EMV 3DS v2.2.0 Core requirement.

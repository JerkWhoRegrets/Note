# EMV 3DS v2.2.0 Source Map

## Source identity

| Field | Value |
|---|---|
| Filename | `EMVCo_3DS_Spec_v220_122018.pdf` |
| Project path | `source/EMVCo_3DS_Spec_v220_122018.pdf` |
| Title | EMV 3-D Secure Protocol and Core Functions Specification |
| Version | 2.2.0 |
| Publication date | December 2018 |
| Byte size | 5,085,320 bytes |
| SHA-256 | `d71b24709df99757f134b022bfe80c34896e622673ee1ef36f23a3381ccbb9a3` |
| Physical PDF page count | 286 |
| PDF metadata title | Not populated |

The title, version, and publication date are taken from the cover and confirmed by the revision log on physical PDF pages 1 and 3. File properties and checksum are calculated from the preserved project copy.

## Physical-page numbering convention

All project citations use the **physical PDF page**, counted from 1 at the cover, as shown by a PDF viewer or extraction page index. This is not the same as the preliminary roman-numeral footer (for example, physical page 4 is labelled `Page iv / xv`). The contents entries in this edition point to physical PDF pages.

## Navigation map

Ranges below are navigation ranges transcribed or bounded from the contents on physical PDF pages 4-11. A range ending immediately before the next listed entry is a working navigation boundary, not a claim about substantive applicability.

### Front matter

| Part | Physical PDF pages |
|---|---:|
| Cover | 1 |
| Legal Notice | 2 |
| Revision Log | 3 |
| Contents | 4-11 |
| Figures | 12 |
| Tables | 13-14 |

### Chapters and top-level sections

| Chapter or section | Title | Physical PDF pages |
|---|---|---:|
| 1 | Introduction | 15-28 |
| 1.1 | Purpose | 15 |
| 1.2 | Audience | 15 |
| 1.3 | Normative References | 16 |
| 1.4 | Acknowledgment | 17 |
| 1.5 | Definitions | 18-25 |
| 1.6 | Abbreviations | 26 |
| 1.7 | 3-D Secure Protocol Version Number | 27 |
| 1.8 | Supporting Documentation | 28 |
| 1.9 | Terminology and Conventions | 28 |
| 2 | EMV 3-D Secure Overview | 29-42 |
| 2.1 | Acquirer Domain | 30-31 |
| 2.2 | Interoperability Domain | 32-33 |
| 2.3 | Issuer Domain | 33-34 |
| 2.4 | 3-D Secure Messages | 34-35 |
| 2.5 | Authentication Flows | 35-36 |
| 2.6 | Frictionless Flow Outline | 37-40 |
| 2.7 | Challenge Flow Outline | 41-42 |
| 3 | EMV 3-D Secure Authentication Flow Requirements | 43-78 |
| 3.1 | App-based Requirements | 43-56 |
| 3.2 | Challenge Flow with OOB Authentication Requirements | 57-58 |
| 3.3 | Browser-based Requirements | 59-70 |
| 3.4 | 3RI-based Requirements | 71-78 |
| 4 | EMV 3-D Secure User Interface Templates, Requirements, and Guidelines | 79-110 |
| 4.1 | 3-D Secure User Interface Templates | 79-80 |
| 4.2 | App-based User Interface Overview | 81-101 |
| 4.3 | Browser-based User Interface Overview | 102-108 |
| 4.4 | 3RI Considerations | 109-110 |
| 5 | EMV 3-D Secure Message Handling Requirements | 111-132 |
| 5.1 | General Message Handling | 111-114 |
| 5.2 | Partial System Outages | 115 |
| 5.3 | 3-D Secure Component Availability | 115 |
| 5.4 | Error Codes | 115 |
| 5.5 | Timeouts | 115-118 |
| 5.6 | PReq/PRes Message Handling Requirements | 119-120 |
| 5.7 | App/SDK-based Message Handling | 121 |
| 5.8 | Browser-based Message Handling | 122-123 |
| 5.9 | Message Error Handling | 124-132 |
| 6 | EMV 3-D Secure Security Requirements | 133-144 |
| 6.1 | Links | 134-137 |
| 6.2 | Security Functions | 138-144 |

### Annexes and requirements index

| Annex or section | Title | Physical PDF pages |
|---|---|---:|
| Annex A | 3-D Secure Data Elements | 145-258 |
| A.1 | Missing Required Fields | 147 |
| A.2 | Field Edit Criteria | 147 |
| A.3 | Encryption of AReq Data | 147 |
| A.4 | EMV 3-D Secure Data Elements | 148-222 |
| A.5 | Detailed Field Values | 223-233 |
| A.6 | Message Extension Data | 234-237 |
| A.7 | 3DS Requestor Risk Information | 237-256 |
| A.8 | UI Data Elements | 257-258 |
| Annex B | Message Format | 259-270 |
| B.1 | AReq Message Data Elements | 259-262 |
| B.2 | ARes Message Data Elements | 262-263 |
| B.3 | CReq Message Data Elements | 264 |
| B.4 | CRes Message Data Elements | 265-266 |
| B.5 | Final CRes Message Data Elements | 266 |
| B.6 | PReq Message Data Elements | 267 |
| B.7 | PRes Message Data Elements | 267 |
| B.8 | RReq Message Data Elements | 268 |
| B.9 | RRes Message Data Elements | 269 |
| B.10 | Error Messages Data Elements | 270 |
| Annex C | Generate ECC Key Pair | 271-272 |
| C.1 | Input | 271 |
| C.2 | Output and Process | 271-272 |
| Annex D | Approved Transport Layer Security Versions | 273 |
| D.1 | Cipher Suites for TLS 1.2 | 273 |
| D.2 | Not Supported | 273 |
| Blank continuation page | Intentionally blank | 274 |
| Requirements | Requirements index | 275-286 |

## Normative-source warning

The PDF in `source/` remains the primary normative source. Markdown and extracted-text files are verified working references for navigation and analysis; they do not replace the PDF. Verify every requirement against the relevant physical PDF page, especially tables, figures, and ambiguous extraction.

## Citation format

- With a requirement number: `[EMV 3DS v2.2.0, section X, p. Y, Req N]`
- Without a requirement number: `[EMV 3DS v2.2.0, section X, p. Y]`

# EMV 3DS Analysis Operating Guide

## Purpose and authority

- This project is a verified, issuer-ACS-focused analysis of **EMV 3-D Secure Protocol and Core Functions Specification, version 2.2.0, December 2018**.
- Normative source: `source/EMVCo_3DS_Spec_v220_122018.pdf`.
- Source identity and SHA-256 checksum: `docs/00-source-map.md`.
- Treat `docs/analysis-status.md` as the verification gate. Do not present the knowledge base as verified unless it records a passing result.

## Routing map

- `docs/00-source-map.md` — source identity, checksum, physical-page convention, section/page navigation, and citation rules.
- `docs/acs-implementation-guide.md` — issuer ACS architecture, responsibilities, state, processing, and implementation boundaries.
- `docs/message-route-matrix.md` — protocol message senders, receivers, routes, variants, and explicitly absent routes.
- `docs/security-matrix.md` — direct-link security, cryptographic responsibilities, trust boundaries, and external gaps.
- `docs/verification-and-otp-design.md` — verification/OTP lifecycle and the boundary between EMV 3DS and issuer-internal services.
- `docs/traceability-matrix.md` — requirement-number and topical traceability into the verified analysis.
- `docs/unresolved-questions.md` — source ambiguities, scheme/vendor dependencies, issuer policy choices, and implementation decisions.

## Answering discipline

- Search the verified Markdown first. Use `docs/00-source-map.md` to navigate, then inspect only the PDF pages cited by the relevant Markdown; inspect the PDF directly for tables, figures, ambiguous extraction, or any claim needing confirmation.
- Every sourced claim must cite the section (or chapter/Annex), **physical PDF page**, and requirement number when one exists. Use `[EMV 3DS v2.2.0, section X, p. Y, Req N]` or, when there is no numbered requirement, `[EMV 3DS v2.2.0, section X, p. Y]`. Use `pp. Y-Z` for a page range.
- Explicitly classify material as **Normative Requirement**, **Interpretation**, **Issuer ACS Implementation Consideration**, **Scheme or Vendor Gap**, or **Unresolved**. Do not turn interpretations or implementation advice into protocol requirements.
- Never invent protocol messages, internal issuer APIs, security guarantees, or scheme requirements. Record absent or insufficient authority as a gap or unresolved question.
- State this scope limitation when currency matters: the December 2018 v2.2.0 PDF may not include later EMVCo bulletins, scheme mandates, certification cases, PCI obligations, or current regulatory requirements.
- Keep detailed technical knowledge in `docs/`; keep this file limited to routing and operating discipline.

## Adding a source

1. Preserve the source under `source/`; record its title, version/date, path, checksum, authority, and relationship to the December 2018 PDF in `docs/00-source-map.md`.
2. Update only the affected analysis, matrices, traceability, and unresolved items, with source-specific citations and clear authority labels.
3. Re-run independent verification and record the result in `docs/analysis-status.md`; until it passes, do not describe the updated knowledge base as verified or finalize routing to it here.

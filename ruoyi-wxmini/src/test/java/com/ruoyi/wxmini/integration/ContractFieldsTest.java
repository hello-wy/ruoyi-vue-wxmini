package com.ruoyi.wxmini.integration;

import com.ruoyi.wxmini.vo.WxSignupUserVo;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Set;
import java.util.TreeSet;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Post-fix contract fields verification for
 * {@code GET /wxmini/jobs/{jobId}/signup-users}.
 *
 * <p>After Task 6.1 (document consolidation):
 * <ul>
 *   <li>The flat-named duplicate {@code jobs__{jobId}__signup-users.md} has been deleted.</li>
 *   <li>The canonical {@code jobs/{jobId}__signup-users.md} is the single source of truth.</li>
 *   <li>Its field set covers all {@link WxSignupUserVo} fields plus {@code payrollPaid}
 *       and {@code payrollItemStatus}.</li>
 *   <li>No field that existed before the fix has been deleted or renamed.</li>
 * </ul>
 *
 * <p>**Validates: Requirements 2.4**
 *
 * <p>Validates: P4 (single source of truth), P5 (Preservation · no field deletion);
 * Requirements: 2.4, 3.7.
 */
class ContractFieldsTest {

    private static final String JSON_FIELD_PATTERN = "\"([a-zA-Z][a-zA-Z0-9_]*)\"\\s*:";

    /**
     * The complete set of required fields that must appear in the contract document.
     * This is the union of WxSignupUserVo fields + payrollPaid + payrollItemStatus.
     */
    private static final Set<String> REQUIRED_FIELDS = Set.of(
            "userInfoId",
            "displayName",
            "phoneMasked",
            "orderNo",
            "attendanceStatus",
            "attendanceStatusLabel",
            "auditStatus",
            "auditStatusLabel",
            "auditRemark",
            "signTime",
            "signedCount",
            "signImageUrl",
            "submitTime",
            "payrollPaid",
            "payrollItemStatus"
    );

    @Test
    @DisplayName("Post-fix · canonical contract exists and covers all required fields (P4)")
    void canonical_contract_covers_all_required_fields() throws IOException {
        Path canonical = locateRepoFile(
                "ruoyi-vue-wxmini/docs/api-contract/wxmini/jobs/{jobId}__signup-users.md");
        assertNotNull(canonical, "canonical contract document must exist on filesystem");
        String content = new String(Files.readAllBytes(canonical), StandardCharsets.UTF_8);

        Set<String> docFields = parseSampleResponseFields(content);

        // Post-fix assertion: canonical document contains ALL required fields
        for (String requiredField : REQUIRED_FIELDS) {
            assertTrue(docFields.contains(requiredField),
                    "POST-FIX: canonical contract must document field `" + requiredField
                            + "`. Detected fields: " + docFields);
        }
    }

    @Test
    @DisplayName("Post-fix · canonical contract covers all WxSignupUserVo bean properties")
    void canonical_contract_covers_WxSignupUserVo_fields() throws IOException {
        Path canonical = locateRepoFile(
                "ruoyi-vue-wxmini/docs/api-contract/wxmini/jobs/{jobId}__signup-users.md");
        assertNotNull(canonical, "canonical contract document must exist on filesystem");
        String content = new String(Files.readAllBytes(canonical), StandardCharsets.UTF_8);

        Set<String> docFields = parseSampleResponseFields(content);
        Set<String> voFields = collectWxSignupUserVoFields();

        // Every WxSignupUserVo field must be documented
        for (String voField : voFields) {
            assertTrue(docFields.contains(voField),
                    "POST-FIX: canonical contract must document VO field `" + voField
                            + "`. Detected fields: " + docFields);
        }

        // Extra documented fields (payrollPaid / payrollItemStatus) must also be present
        assertTrue(docFields.contains("payrollPaid"),
                "POST-FIX: canonical contract must document payrollPaid");
        assertTrue(docFields.contains("payrollItemStatus"),
                "POST-FIX: canonical contract must document payrollItemStatus");
    }

    @Test
    @DisplayName("Post-fix · flat-named duplicate contract has been deleted (P4 single source)")
    void flat_duplicate_contract_deleted() {
        Path flat = locateRepoFile(
                "ruoyi-vue-wxmini/docs/api-contract/wxmini/jobs__{jobId}__signup-users.md");
        assertNull(flat,
                "POST-FIX: the flat-named duplicate contract `jobs__{jobId}__signup-users.md` "
                        + "must have been deleted by Task 6.1. Only the canonical version "
                        + "`jobs/{jobId}__signup-users.md` should remain.");
    }

    @Test
    @DisplayName("Post-fix · no pre-existing field has been deleted or renamed (P5 preservation)")
    void no_preexisting_field_deleted() throws IOException {
        Path canonical = locateRepoFile(
                "ruoyi-vue-wxmini/docs/api-contract/wxmini/jobs/{jobId}__signup-users.md");
        assertNotNull(canonical);
        String content = new String(Files.readAllBytes(canonical), StandardCharsets.UTF_8);

        Set<String> docFields = parseSampleResponseFields(content);

        // These fields existed in the pre-fix baseline (from Task 2 preservation-baseline.md).
        // None of them may be removed or renamed.
        Set<String> preFixFields = new TreeSet<>();
        preFixFields.add("userInfoId");
        preFixFields.add("displayName");
        preFixFields.add("phoneMasked");
        preFixFields.add("orderNo");
        preFixFields.add("attendanceStatus");
        preFixFields.add("attendanceStatusLabel");
        preFixFields.add("signImageUrl");
        preFixFields.add("auditStatus");
        preFixFields.add("auditStatusLabel");
        preFixFields.add("auditRemark");
        preFixFields.add("signTime");
        preFixFields.add("signedCount");
        preFixFields.add("submitTime");
        preFixFields.add("payrollPaid");
        preFixFields.add("payrollItemStatus");

        for (String field : preFixFields) {
            assertTrue(docFields.contains(field),
                    "PRESERVATION: pre-existing field `" + field
                            + "` must not be deleted or renamed in the canonical contract. "
                            + "Detected fields: " + docFields);
        }
    }

    // -- helpers --------------------------------------------------------------

    /**
     * Parse the JSON sample inside the markdown contract: collect every
     * top-level key inside the first {@code ```json} fence.
     */
    private Set<String> parseSampleResponseFields(String markdown) {
        Set<String> fields = new TreeSet<>();
        int fenceStart = markdown.indexOf("```json");
        if (fenceStart < 0) {
            return fields;
        }
        int bodyStart = markdown.indexOf('\n', fenceStart);
        int fenceEnd = markdown.indexOf("```", bodyStart + 1);
        if (bodyStart < 0 || fenceEnd < 0) {
            return fields;
        }
        String body = markdown.substring(bodyStart, fenceEnd);
        Pattern p = Pattern.compile(JSON_FIELD_PATTERN);
        Matcher m = p.matcher(body);
        while (m.find()) {
            fields.add(m.group(1));
        }
        return fields;
    }

    private Set<String> collectWxSignupUserVoFields() {
        Set<String> fields = new TreeSet<>();
        try {
            for (PropertyDescriptor pd : Introspector.getBeanInfo(WxSignupUserVo.class, Object.class)
                    .getPropertyDescriptors()) {
                fields.add(pd.getName());
            }
        } catch (IntrospectionException e) {
            throw new IllegalStateException("could not introspect WxSignupUserVo", e);
        }
        return fields;
    }

    private Path locateRepoFile(String relativeFromRepoRoot) {
        Path cursor = Paths.get("").toAbsolutePath();
        for (int i = 0; i < 6 && cursor != null; i++) {
            Path candidate = cursor.resolve(relativeFromRepoRoot);
            if (Files.exists(candidate)) {
                return candidate;
            }
            cursor = cursor.getParent();
        }
        // Try walking down from cwd to find ruoyi-vue-wxmini sibling layouts as well.
        Path cwd = Paths.get("").toAbsolutePath();
        for (Path p = cwd; p != null; p = p.getParent()) {
            Path direct = p.resolve(relativeFromRepoRoot);
            if (Files.exists(direct)) {
                return direct;
            }
            // Also consider the path with the leading 'ruoyi-vue-wxmini/' stripped.
            if (relativeFromRepoRoot.startsWith("ruoyi-vue-wxmini/")) {
                Path stripped = p.resolve(relativeFromRepoRoot.substring("ruoyi-vue-wxmini/".length()));
                if (Files.exists(stripped)) {
                    return stripped;
                }
            }
        }
        return null;
    }
}

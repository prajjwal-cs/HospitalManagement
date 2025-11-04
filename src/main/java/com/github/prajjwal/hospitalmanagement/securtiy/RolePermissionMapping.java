package com.github.prajjwal.hospitalmanagement.securtiy;

import com.github.prajjwal.hospitalmanagement.model.type.PermissionType;
import com.github.prajjwal.hospitalmanagement.model.type.RoleType;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import static com.github.prajjwal.hospitalmanagement.model.type.PermissionType.*;
import static com.github.prajjwal.hospitalmanagement.model.type.RoleType.*;

public class RolePermissionMapping {

    private static final Map<RoleType, Set<PermissionType>> map = Map.of(
            PATIENT, Set.of(PATIENT_READ, APPOINTMENT_READ, APPOINTMENT_WRITE),
            DOCTOR, Set.of(APPOINTMENT_DELETE, APPOINTMENT_WRITE, APPOINTMENT_READ, PATIENT_READ),
            ADMIN, Set.of(PATIENT_WRITE, PATIENT_READ, APPOINTMENT_READ, APPOINTMENT_WRITE, APPOINTMENT_DELETE,
                    USER_MANAGE, REPORT_VIEW)
    );


    public static Set<SimpleGrantedAuthority> grantedAuthoritiesForRole(RoleType role) {
        return map.get(role).stream()
                .map(permissionType -> new SimpleGrantedAuthority(permissionType.getPermission()))
                .collect(Collectors.toSet());
    }
}
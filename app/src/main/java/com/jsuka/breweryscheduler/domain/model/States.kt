package com.jsuka.breweryscheduler.domain.model

/**
 * Run-time states for a single employee assignment.
 * Formalized in the Module 4 state machine diagram.
 */
enum class AssignmentState {
    DRAFT,
    PENDING_VALIDATION,
    VALID,
    CONFLICT,
    PUBLISHED
}

/** Run-time states for a whole weekly schedule. */
enum class ScheduleState {
    DRAFT,
    UNDER_REVIEW,
    PUBLISHED,
    ARCHIVED
}

/** Run-time states for a user session. */
enum class SessionState {
    LOGGED_OUT,
    AUTHENTICATING,
    AUTHENTICATED,
    SESSION_EXPIRED
}

/** Distinguishes a Supervisor from an Employee for role-based access. */
enum class PersonRole {
    EMPLOYEE,
    SUPERVISOR
}

/**
 * Credential codes required by job positions.
 * FORK_TRUCK is the only certification that expires; the rest are
 * qualifications, which per the Module 3 design do not lapse.
 */
object CredentialCodes {
    const val PALLETIZER = "PALLETIZER"
    const val FORK_TRUCK = "FORK_TRUCK"
    const val FILLER = "FILLER"
    const val PACKER = "PACKER"
    const val BREWHOUSE = "BREWHOUSE"
    const val FERMENTING = "FERMENTING"
    const val FILTER = "FILTER"
    const val BRIGHT_BEER = "BRIGHT_BEER"
}

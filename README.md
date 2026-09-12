# Brewery Workforce Scheduling and Qualification Management System

CSC480 Capstone Project. Native Android application in Kotlin implementing
the design specified in Modules 3 through 6.

## Build stages

This project is being assembled in stages so each layer can be compiled and
verified before the next is added.

- **Stage 1 (done)** — Gradle setup, Room data layer, seed data,
  `IRepository<T>` implementations, and a verification screen that reads
  every seeded table back through the repositories.
- **Stage 2 (done)** — the four `IValidationRule` implementations,
  `ConflictValidator`, `SchedulingEngine`, `RecommendationService`,
  `ReportGenerator`, and JUnit tests written against the Module 6 test plan.
- **Stages 3 and 4 (done)** — login with the session state machine,
  supervisor schedule grid, assignment creation, conflict display with
  ranked alternatives, supervisor override, schedule publication, employee
  schedule view, availability submission, and the three reports.

## Screens

| Screen | Who sees it | What it demonstrates |
|---|---|---|
| Sign in | Everyone | `UserAccount` state machine; generic failure message that does not reveal whether the username exists |
| Schedule grid | Supervisor | Assignment creation, validation results, conflict reasons, ranked alternatives, override, publish |
| Reports | Supervisor | Staffing coverage, qualification gaps, expiring certifications, with generation time shown |
| My schedule | Employee | Published shifts only, plus availability submission |

Role decides the experience: a supervisor gets the grid and reports, an
employee gets a read-only schedule and the availability form. That is the
role-based access described in Module 2.

## Validation rules

| Rule | Checks | Boundary |
|---|---|---|
| `QualificationRule` | Employee holds the credential the position requires | Credential earned after the shift date is not yet held |
| `CertificationExpirationRule` | Required certification has not lapsed | Expiring **on** the shift date passes; the day before fails |
| `AvailabilityRule` | Shift falls inside a submitted window and does not overlap another assignment | A shift ending exactly when another begins is not an overlap |
| `StaffingLevelRule` | Pulling the employee away does not drop their home department below minimum | Exactly at minimum passes; one below fails |

`ConflictValidator` wraps each rule's `evaluate` call in its own try/catch,
so a rule that throws becomes a conflict for that one assignment while the
remaining rules still run. Failures are collected rather than
short-circuited, so a supervisor sees every reason an assignment was
rejected.

## Tests

Run with `./gradlew test`. No emulator needed; the rules are pure functions
of a resolved `ProposedAssignment`, so they run on the JVM.

- `ModuleSixTestPlanTest` maps one-to-one onto the rows of Table 1 in the
  Module 6 system test plan, including both boundary pairs.
- `ValidationRuleTest` exercises each rule in isolation.
- `PasswordHasherTest` covers the salted PBKDF2 hashing.

## Domain model

Four departments, each with a minimum staffing level of 2:

| Department | Positions |
|---|---|
| Warehouse | Palletizer Operator, Fork Truck Operator |
| Production | Filler Operator, Packer Operator |
| Hot Side Brewing | Brewhouse Operator, Fermenting Operator |
| Cold Side Brewing | Filter Operator, Bright Beer Operator |

Twelve employees are seeded, three per department, plus one supervisor.
Fork Truck Operator is the only position requiring a certification that
expires; the other seven require qualifications, which do not lapse.

## Seeded boundary cases

Credential dates are anchored to the current date so the boundary pair from
the Module 6 test plan is always live:

- **Dylan Reyes** holds a fork truck certification expiring **today**, which
  must pass at the boundary.
- **Marcus Webb** holds one that expired **yesterday**, which must fail.
- **Priya Raman** is available on swing shift only, giving
  `AvailabilityRule` a real failure case on a day shift.

## Demo accounts

| Username | Password | Role |
|---|---|---|
| `dwhitfield` | `Supervisor#1` | Supervisor |
| `employee` | `Employee#1` | Employee |

Passwords are stored only as salted PBKDF2 hashes.

## Running

1. Open the project folder in Android Studio.
2. Let Gradle sync. Accept any prompt from the AGP Upgrade Assistant if your
   Android Studio is newer than the versions pinned in
   `gradle/libs.versions.toml`.
3. Run the `app` configuration on an emulator or device running API 26 or
   higher.
4. Run the unit tests with `./gradlew test`.

On first launch the database seeds itself. Clearing app storage and
relaunching re-seeds it.

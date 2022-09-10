package com.denisgithuku.softkeja.presentation.components.reset_password

import androidx.annotation.StringRes
import com.denisgithuku.softkeja.R

enum class PasswordRequirements {
    CAPITAL_LETTER,
    SIX_CHARACTERS,
    DIGIT
}

enum class PasswordRequirement(
    @StringRes val label: Int
) {
    CAPITAL_LETTER(R.string.password_requirement_capital),
    SIX_CHARACTERS(R.string.password_requirement_six_characters),
    DIGIT(R.string.password_requirement_digit)
}
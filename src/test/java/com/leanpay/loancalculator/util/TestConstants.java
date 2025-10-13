package com.leanpay.loancalculator.util;


public final class TestConstants {

    private TestConstants() {
    }

    public static final String VALID_REQUEST = """
            {
              "amount": 1000,
              "annualInterestPercent": 5,
              "numberOfMonths": 10
            }
            """;

    public static final String ZERO_INTEREST_REQUEST = """
            {
              "amount": 1200,
              "annualInterestPercent": 0,
              "numberOfMonths": 6
            }
            """;

    public static final String INVALID_AMOUNT_REQUEST = """
            {
              "amount": 0,
              "annualInterestPercent": 5,
              "numberOfMonths": 10
            }
            """;

    public static final String MISSING_FIELD_REQUEST = """
            {
              "annualInterestPercent": 5,
              "numberOfMonths": 10
            }
            """;
}


Feature: Get a raw price form Yahoo!
  Scenario: Get a quote ratio from Yahoo! Finance
    When An user ask to get a price ratio for "AUDKRW=X" from the system
    Then The user receives "0.00109062"

  Scenario: Get a quote price from Yahoo! Finance
    When An user ask to get a price for "AUDKRW=X" from the system
    Then The user receives "916.91"



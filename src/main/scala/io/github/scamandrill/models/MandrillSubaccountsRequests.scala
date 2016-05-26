package io.github.scamandrill.models

/**
  * Subaccounts to filter
  *
  * @param q   - an optional prefix to filter the subaccounts' ids and names
  */
case class MSubaccountList(q: String) extends MandrillRequest

/**
  * The information about a subaccount
  *
  * @param id  - a unique identifier for the subaccount to be used in sending calls
  */
case class MSubaccountInfo(id: String) extends MandrillRequest

/**
  * A subaccount
  *
  * @param id           - a unique identifier for the subaccount to be used in sending calls
  * @param name         - an optional display name to further identify the subaccount
  * @param notes        - optional extra text to associate with the subaccount
  * @param custom_quota - an optional manual hourly quota for the subaccount. If not specified, Mandrill will manage this based on reputation
  */
case class MSubaccount(id: String,
                       name: String,
                       notes: String,
                       custom_quota: Int) extends MandrillRequest

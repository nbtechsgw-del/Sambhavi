# Test Cases

| ID | Scenario | Steps | Expected Result |
| --- | --- | --- | --- |
| TC-01 | Register user | Open app, choose Register, enter name/email/password | User is logged in and dashboard opens |
| TC-02 | Login user | Use `demo@expense.local` / `password` | Dashboard loads seeded data |
| TC-03 | Add category | Open Categories, enter name and type, save | Category appears in list and dropdowns |
| TC-04 | Edit category | Click edit, change category name, save | Updated name appears everywhere |
| TC-05 | Prevent deleting used category | Delete a category used by transactions | Error message is shown |
| TC-06 | Add expense | Open Transactions, choose EXPENSE category, amount/date, save | Expense appears and dashboard totals update |
| TC-07 | Add income | Open Transactions, choose INCOME category, amount/date, save | Income appears and dashboard totals update |
| TC-08 | Edit transaction | Click edit on transaction, change amount, update | Table and reports use new amount |
| TC-09 | Delete transaction | Click delete on transaction | Transaction is removed and totals update |
| TC-10 | Search | Type search text and press Enter | Matching descriptions/categories are shown |
| TC-11 | Filter | Select date/type/category filters | Table and report data match filters |
| TC-12 | Reports | Open Reports | Category pie chart and income/expense bar chart render |
| TC-13 | Export | Click download button | CSV file is downloaded |
| TC-14 | Dark mode | Click theme icon | Theme changes and persists after refresh |
| TC-15 | Logout | Click logout icon | Session clears and login screen appears |

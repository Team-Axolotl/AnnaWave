package bg.o.sim.annawave.ui

import android.graphics.Color
import android.os.Bundle
import com.google.android.material.card.MaterialCardView
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView

import bg.o.sim.annawave.R
import bg.o.sim.annawave.storage.loggedInPerson

class DashboardReportFragment : androidx.fragment.app.Fragment() {

    companion object {
        fun newInstance() = DashboardReportFragment()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.dashboard_report_fragment, container, false)

        val cardLoanApps: MaterialCardView = view.findViewById(R.id.card_report_loan_apps)
        val cardLoans: MaterialCardView = view.findViewById(R.id.card_report_loans)
        val cardAccounts: MaterialCardView = view.findViewById(R.id.card_report_accounts)

        cardLoanApps.findViewById<ImageView>(R.id.icon).setImageDrawable(activity!!.getDrawable(R.drawable.applications))
        cardLoans.findViewById<ImageView>(R.id.icon).setImageDrawable(activity!!.getDrawable(R.drawable.loans))
        cardAccounts.findViewById<ImageView>(R.id.icon).setImageDrawable(activity!!.getDrawable(R.drawable.piggy_bank))

        cardLoanApps.findViewById<View>(R.id.top_half).setBackgroundResource(R.drawable.gradient_card_loan_apps)
        cardLoans.findViewById<View>(R.id.top_half).setBackgroundResource(R.drawable.gradient_card_loans)
        cardAccounts.findViewById<View>(R.id.top_half).setBackgroundResource(R.drawable.gradient_card_accounts)

        val loanAppsLabel =cardLoanApps.findViewById<TextView>(R.id.text_title)
        val loansLabel = cardLoans.findViewById<TextView>(R.id.text_title)
        val accountsLabel = cardAccounts.findViewById<TextView>(R.id.text_title)

        loanAppsLabel.setTextColor(Color.WHITE)
        loansLabel.setTextColor(Color.WHITE)
        accountsLabel.setTextColor(Color.WHITE)

        loanAppsLabel.setText(R.string.label_loan_applications)
        loansLabel.setText(R.string.label_loans)
        accountsLabel.setText(R.string.label_accounts)

        val loanAppsCount = cardLoanApps.findViewById<TextView>(R.id.text_count)
        val loansCount = cardLoans.findViewById<TextView>(R.id.text_count)
        val accountsCount = cardAccounts.findViewById<TextView>(R.id.text_count)

        loanAppsCount.setTextColor(Color.parseColor("#1b7efa"))
        loansCount.setTextColor(Color.parseColor("#ff7968"))
        accountsCount.setTextColor(Color.parseColor("#8130c9"))

        loanAppsCount.text = "${loggedInPerson?.loanApplicationCount}"
        loansCount.text = "${loggedInPerson?.loanCount}"
        accountsCount.text = "${loggedInPerson?.accountCount}"

        return view
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
    }

}

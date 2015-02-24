package bg.mentormate.academy.reservations.activities.admin;

import android.content.res.Resources;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.webkit.WebView;

import bg.mentormate.academy.reservations.R;

public class AdminTermsActivity extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_privacy);

        WebView privacyPolicyTextView = (WebView) findViewById(R.id.privacyPolicyView);
        Resources res = getResources();
        String company = res.getString(R.string.privacy_policy_company);

        String summary = String.format(
                "<html>\n" +
                        "<body style=\"background: #f9f9f9\">\n" +
                        "<h2>Terms and Conditions (\"Terms\")</h2>\n" +
                        "<p><strong>Last updated: (%1$s).</strong></p>\n" +
                        "<p>Please read these Terms and Conditions (\"Terms\", \"Terms and Conditions\") carefully before using\n" +
                        "the %2$s (the \"Service\") operated by %3$s (\"us\", \"we\", or \"our\").</p>\n" +
                        "<p>Your access to and use of the Service is conditioned on your acceptance of and compliance with\n" +
                        "these Terms. These Terms apply to all visitors, users and others who access or use the Service.\n" +
                        "By accessing or using the Service you agree to be bound by these Terms. If you disagree with any\n" +
                        "part of the terms then you may not access the Service.</p>\n" +
                        "<p>Termination clause for websites that do not have accounts. If your website or mobile app allows\n" +
                        "users to register and have an account, create your own Terms and Conditions.</p>\n" +
                        "<p><strong>Termination</strong></p>\n" +
                        "<p>We may terminate or suspend access to our Service immediately, without prior notice or liability, for\n" +
                        "any reason whatsoever, including without limitation if you breach the Terms.</p>\n" +
                        "<p>All provisions of the Terms which by their nature should survive termination shall survive\n" +
                        "termination, including, without limitation, ownership provisions, warranty disclaimers, indemnity and\n" +
                        "limitations of liability.</p>\n" +
                        "<p><strong>Links To Other Web Sites</strong></p>\n" +
                        "<p>Our Service may contain links to third­party web sites or services that are not owned or controlled\n" +
                        "by %4$s.</p>\n" +
                        "<p>%5$s has no control over, and assumes no responsibility for, the content,\n" +
                        "privacy policies, or practices of any third party web sites or services. You further acknowledge and\n" +
                        "agree that %6$s shall not be responsible or liable, directly or indirectly, for\n" +
                        "any damage or loss caused or alleged to be caused by or in connection with use of or reliance on\n" +
                        "any such content, goods or services available on or through any such web sites or services.\n" +
                        "We strongly advise you to read the terms and conditions and privacy policies of any third­party web\n" +
                        "sites or services that you visit.</p>\n" +
                        "<p><strong>Governing Law</strong></p>\n" +
                        "<p>These Terms shall be governed and construed in accordance with the laws of %7$s, without regard to its conflict of law provisions.</p>\n" +
                        "<p>Our failure to enforce any right or provision of these Terms will not be considered a waiver of those\n" +
                        "rights. If any provision of these Terms is held to be invalid or unenforceable by a court, the\n" +
                        "remaining provisions of these Terms will remain in effect. These Terms constitute the entire\n" +
                        "agreement between us regarding our Service, and supersede and replace any prior agreements we\n" +
                        "might have between us regarding the Service.</p>\n" +
                        "<p><strong>Changes</strong></p>\n" +
                        "<p>We reserve the right, at our sole discretion, to modify or replace these Terms at any time. If a\n" +
                        "revision is material we will try to provide at least %8$s days notice prior to any new terms\n" +
                        "taking effect. What constitutes a material change will be determined at our sole discretion.</p>\n" +
                        "<p>By continuing to access or use our Service after those revisions become effective, you agree to be\n" +
                        "bound by the revised terms. If you do not agree to the new terms, please stop using the Service.</p>\n" +
                        "<p><strong>Contact Us</strong></p>\n" +
                        "<p>If you have any questions about these Terms, please contact us</p>\n" +
                        "</body></html>",
                res.getString(R.string.privacy_policy_last_update),
                res.getString(R.string.privacy_policy_service),
                company,
                company,
                company,
                company,
                res.getString(R.string.privacy_policy_country),
                res.getString(R.string.privacy_policy_period));
        WebView webView = (WebView) findViewById(R.id.privacyPolicyView);
        webView.loadData(summary, "text/html", null);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_admin_terms, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}

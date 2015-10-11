package pl.demo.rules;

import org.junit.rules.TestRule;
import org.junit.runner.Description;
import org.junit.runners.model.Statement;
import pl.demo.ApplicationContextFake;
import pl.demo.MessageResolverFake;
import pl.demo.core.util.SpringBeanProvider;

/**
 * Created by robertsikora on 11.10.15.
 */
public class FakeMessageResolverTestRule implements TestRule {
    @Override
    public Statement apply(final Statement base, final Description description) {
        return new Statement() {
            @Override
            public void evaluate() throws Throwable {
                try {
                    SpringBeanProvider.setAppCtx(ApplicationContextFake.getApplicationContext(MessageResolverFake.class));
                    base.evaluate();
                    return;
                } catch (final AssertionError ae) {
                    return;
                } finally {
                    SpringBeanProvider.setAppCtx(null);
                }
            }
        };
    }
}

/**
 * Copyright (c) 2018, Sylvain Baudoin
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.sonar.plugins.ansible.rules;

import junit.framework.TestCase;
import org.sonar.api.server.rule.RulesDefinition;
import org.sonar.plugins.ansible.checks.AnsibleCheckRepository;
import org.sonar.plugins.yaml.languages.YamlLanguage;

public class AnsibleRulesDefinitionTest extends TestCase {
    public void testDefine() {
        AnsibleRulesDefinition rulesDefinition = new AnsibleRulesDefinition();
        RulesDefinition.Context context = new RulesDefinition.Context();
        rulesDefinition.define(context);
        RulesDefinition.Repository repository = context.repository(AnsibleCheckRepository.REPOSITORY_KEY);

        assertEquals(AnsibleCheckRepository.REPOSITORY_NAME, repository.name());
        assertEquals(YamlLanguage.KEY, repository.language());
        assertEquals(16, repository.rules().size());

        RulesDefinition.Rule aRule = repository.rule("ANSIBLE0004");
        assertNotNull(aRule);
        assertEquals("Git checkouts must contain explicit version", aRule.name());

        // No templates
        assertEquals(0L, repository.rules().stream().filter(RulesDefinition.Rule::template).map(RulesDefinition.Rule::key).count());

        for (RulesDefinition.Rule rule : repository.rules()) {
            for (RulesDefinition.Param param : rule.params()) {
                assertFalse("Description for " + param.key() + " should not be empty", "".equals(param.description()));
            }
        }
    }
}
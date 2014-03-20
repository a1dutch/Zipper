/*
 * Copyright 2014 Andrew Holland.
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

package uk.co.a1dutch.zipper;

import java.io.File;
import java.io.IOException;

import org.apache.commons.io.FileUtils;
import org.junit.Before;

/**
 * @author a1dutch
 */
public abstract class AbstractArchiverTest {

    protected static final String TEST_DATA_DIR = "target/test-data";

    protected static final String ARCHIVES_DIR = "target/test-classes/archives";

    @Before
    public void deleteTestDataDirectory() throws IOException {
        FileUtils.deleteDirectory(new File(TEST_DATA_DIR));
    }
}

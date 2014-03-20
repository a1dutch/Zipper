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

/**
 * Utility class with common archive functions.
 * 
 * @author a1dutch
 * @since 1.1.0
 */
final class ArchiveUtils {
    /**
     * Normalises a path by replacing all back slashes with forward slashes.
     * 
     * @param path
     *            the path to normalise.
     * @return the normalised path.
     */
    static String normalisePath(String path) {
        return path.replace('\\', '/');
    }
}

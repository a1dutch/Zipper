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
 * Exception to be thrown when zipping an archive.
 * 
 * @author a1dutch
 * @since 1.1.0
 */
@SuppressWarnings("serial")
public class ZipperRuntimeException extends RuntimeException {
    
    /**
     * Constructs a new zipper runtime exception with the given message and cause.
     * 
     * @param message the detailed message.
     * @param cause the cause.
     */
    public ZipperRuntimeException(String message, Throwable cause) {
        super(message, cause);
    }

    /**
     * Constructs a new zipper runtime exception with the given message.
     * 
     * @param message the detailed message.
     */
    public ZipperRuntimeException(String message) {
        super(message);
    }

    /**
     * Constructs a new zipper runtime exception with the given cause.
     * 
     * @param cause the cause.
     */
    public ZipperRuntimeException(Throwable cause) {
        super(cause.getMessage(), cause);
    }
}

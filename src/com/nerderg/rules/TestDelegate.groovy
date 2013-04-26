/* Copyright 2010, 2011 Peter McNeil

This file is part of One Ring.

Licensed under the Apache License, Version 2.0 (the "License"); you may not
use this file except in compliance with the License. You may obtain a copy
of the License at http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License. */
package com.nerderg.rules

/**
 * User: pmcneil
 * Date: 4/02/11
 *
 */
class TestDelegate {

    Map fact
    List<String> errors = []

    def methodMissing(String name, args) {
        return compareValues(fact[name], args[0])
    }

    private boolean compareMaps(Map m1, Map m2) {
        boolean result = true
        m2.each { m2kvp ->
            if (!m1.containsKey(m2kvp.key)) {
                errors.add "Value $m2kvp.key not found."
                result = false
            } else {
                result = result && compareValues(m2kvp.value, m1[m2kvp.key])
            }
        }
        return result
    }

    private boolean compareLists(List l1, List l2) {
        boolean result = true
        l1.eachWithIndex {el, i ->
            result = result && compareValues(el, l2[i])
        }
        return result
    }

    private boolean compareValues(v1, v2) {
        if (v1 instanceof Map) {
            if (!compareMaps(v1, v2)) {
                errors.add "Maps are not equal:"
                errors.add "Fact:     $v1\nExpected: $v2"
                return false
            }
            return true
        } else if (v1 instanceof List) {
            if (!compareLists(v1, v2)) {
                errors.add "Lists are not equal:"
                errors.add "Fact:     $v1\nExpected: $v2"
                return false
            }
            return true
        } else {
            if (v1 != v2) {
                errors.add "Values are not equal:"
                errors.add "(${v1?.class}) $v1 != (${v2?.class}) $v2"
                return false
            }
            return true
        }
    }

}

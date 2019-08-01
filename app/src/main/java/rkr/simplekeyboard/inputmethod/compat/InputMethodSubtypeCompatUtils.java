/*
 * Copyright (C) 2013 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package rkr.simplekeyboard.inputmethod.compat;

import android.os.Build;
import android.text.TextUtils;
import android.view.inputmethod.InputMethodSubtype;

import java.util.Locale;

import rkr.simplekeyboard.inputmethod.latin.common.LocaleUtils;

import java.lang.reflect.Constructor;

public final class InputMethodSubtypeCompatUtils {
    private InputMethodSubtypeCompatUtils() {
        // This utility class is not publicly instantiable.
    }

    private static final Constructor<?> CONSTRUCTOR_INPUT_METHOD_SUBTYPE =
            CompatUtils.getConstructor(InputMethodSubtype.class,
                    int.class, int.class, String.class, String.class, String.class, boolean.class,
                    boolean.class, int.class);

    public static Locale getLocaleObject(final InputMethodSubtype subtype) {
        // Locale.forLanguageTag() is available only in Android L and later.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            final String languageTag = subtype.getLanguageTag();
            if (!TextUtils.isEmpty(languageTag)) {
                return Locale.forLanguageTag(languageTag);
            }
        }
        return LocaleUtils.constructLocaleFromString(subtype.getLocale());
    }

    public static InputMethodSubtype newInputMethodSubtype(int nameId, int iconId, String locale,
                                                           String mode, String extraValue, boolean isAuxiliary,
                                                           boolean overridesImplicitlyEnabledSubtype, int id) {
        if (CONSTRUCTOR_INPUT_METHOD_SUBTYPE == null
                || Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN_MR1) {
            return new InputMethodSubtype(nameId, iconId, locale, mode, extraValue, isAuxiliary,
                    overridesImplicitlyEnabledSubtype);
        }
        return (InputMethodSubtype) CompatUtils.newInstance(CONSTRUCTOR_INPUT_METHOD_SUBTYPE,
                nameId, iconId, locale, mode, extraValue, isAuxiliary,
                overridesImplicitlyEnabledSubtype, id);
    }
}

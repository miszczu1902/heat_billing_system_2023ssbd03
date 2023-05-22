import i18n from "i18next";
import {initReactI18next} from "react-i18next";
import LanguageDetector from "i18next-browser-languagedetector";
import HttpApi from "i18next-http-backend";
import translationEN from './assets/lang/en/translation.json';
import translationPL from './assets/lang/pl/translation.json';

export const resources = {
    pl: {
        translation: translationPL
    },
    en: {
        translation: translationEN
    }
};

i18n.use(initReactI18next)
    .use(LanguageDetector)
    .use(HttpApi)
    .init({
        resources,
        fallbackLng: "pl",
        detection: {
            order: ["cookie", "localStorage", "htmlTag", "path", "subdomain"],
            caches: ["cookie", "localStorage"],
        }
    });

export default i18n;
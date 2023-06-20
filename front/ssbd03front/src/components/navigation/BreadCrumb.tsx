import {Breadcrumbs, Link} from "@mui/material";
import Typography from "@mui/material/Typography";
import React, {useState} from "react";
import {useLocation, useNavigate, useParams} from "react-router-dom";
import {useTranslation} from "react-i18next";
import {MANAGER, OWNER} from "../../consts";
import HomeIcon from "../icons/HomeIcon";

const BreadCrumb = () => {
    const location = useLocation();
    const params = useParams();
    const {t, i18n} = useTranslation();
    const [currentRole, setCurrentRole] = useState(localStorage.getItem("role"));
    const username = params.username;
    const building = params.buildingId;
    const report = params.reportId;
    const place = params.placeId;
    const year = params.year;

    return (
        <div style={{color: '#ffffff'}}>
            <Typography variant="h6" sx={{
                marginLeft: '4vh',
                marginRight: 'auto',
                color: '#ffffff'
            }}>
                <Breadcrumbs aria-label="breadcrumb">
                    <Link underline="hover" color="#ffffff" href="/">
                        <HomeIcon/>
                    </Link>
                    {(location.pathname.includes("/accounts") && localStorage.getItem("role") !== OWNER) && <Link
                        underline="hover"
                        color="#ffffff"
                        href="/accounts/"
                    >{t('breadcrumb.accounts')}</Link>}
                    {(location.pathname.includes("/accounts/") && !location.pathname.includes("/accounts/self")) &&
                        <Link
                            underline="hover"
                            color="#ffffff"
                            href={"/accounts/" + username}
                        >{t('breadcrumb.account')}</Link>}
                    {location.pathname.includes("/accounts/self") && <Link
                        underline="hover"
                        color="#ffffff"
                        href={"/accounts/self/" + currentRole?.toLowerCase()}
                    >{t('breadcrumb.self')}</Link>}
                    {location.pathname.includes("/buildings") && <Link
                        underline="hover"
                        color="#ffffff"
                        href={"/buildings"}>
                        {t('breadcrumb.buildings')}</Link>}
                    {(location.pathname.includes("/buildings/") && location.pathname.includes("/buildings/building")) && <Link
                        underline="hover"
                        color="#ffffff"
                        href={"/buildings/" + building}>
                        {t('breadcrumb.building')}</Link>}
                    {(location.pathname.includes("/buildings/") && !location.pathname.includes("/buildings/building")) && <Link
                        underline="hover"
                        color="#ffffff"
                        href={"/buildings/building/" + building}>
                        {t('breadcrumb.building')}</Link>}
                    {location.pathname.includes("/annual-balance") && <Link
                        underline="hover"
                        color="#ffffff"
                        href={"/buildings/" + building + "/" + report + "/" + place + "/" + year}>
                        {t('breadcrumb.annualBalance')}</Link>}
                    {location.pathname.includes("/places") && <Link
                        underline="hover"
                        color="#ffffff"
                        href={currentRole === MANAGER ? "/buildings/" : "/places/self"}>
                        {t('breadcrumb.places')}</Link>}
                    {location.pathname.includes("/places/place/") && <Link
                        underline="hover"
                        color="#ffffff"
                        href={"/places/place/" + place}>
                        {t('breadcrumb.place')}</Link>}
                    {location.pathname.includes("/places/self/") && <Link
                        underline="hover"
                        color="#ffffff"
                        href={"/places/self/" + place}>
                        {t('breadcrumb.places')}</Link>}
                    {(location.pathname.includes("/manage") && !location.pathname.includes("/manager")) && <Link
                        underline="hover"
                        color="#ffffff"
                        href={"/manage"}>
                        {t('breadcrumb.manage')}</Link>}
                    {location.pathname.includes("/annual-reports/self") && <Link
                        underline="hover"
                        color="#ffffff"
                        href={"/annual-reports/self"}>
                        {t('breadcrumb.place')}</Link>}
                    {location.pathname.includes("/annual-reports/self/") && <Link
                        underline="hover"
                        color="#ffffff"
                        href={"/annual-reports/self/annual-balance/" + report + "/" + place + "/" + year}>
                        {t('breadcrumb.annualBalance')}</Link>}
                </Breadcrumbs>
            </Typography>
        </div>
    );
}
export default BreadCrumb;
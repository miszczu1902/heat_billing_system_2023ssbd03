import {useLocation, useNavigate, useParams} from "react-router-dom";
import {useTranslation} from "react-i18next";
import React, {useEffect, useState} from "react";
import {API_URL} from "../../../consts";
import axios from "axios";
import {Button, Paper, Table, TableBody, TableCell, TableContainer, TableHead, TableRow} from "@mui/material";
import RefreshIcon from "../../icons/RefreshIcon";
import {HotWaterEntryForList} from "../../../types/HotWaterEntryForList";
import { format } from 'date-fns';
import ModifyHotWaterEntry from "./ModifyHotWaterEntry";
import InsertHotWaterEntry from "./InsertHotWaterEntry";

const HotWaterEntriesList = () => {
    const location = useLocation();
    const params = useParams();
    const {t, i18n} = useTranslation();
    const navigate = useNavigate();
    const token = 'Bearer ' + localStorage.getItem("token");
    const [hotWaterEntries, setHotWaterEntries] = useState<HotWaterEntryForList[]>([]);
    const placeId = params.placeId;
    const URL = `${API_URL}/heat-distribution-centre/hot-water-consumption/place/${placeId}`;
    const currentDate = new Date();
    const formattedDate = format(currentDate, 'yyyy-MM');

    const fetchData = async () => {
        axios.get(URL, {
            headers: {
                Authorization: token
            }
        }).then(response => {
            setHotWaterEntries(response.data);
        }).catch(error => {
            if (error.response.status == 403) navigate('/');
        })
    };

    useEffect(() => {
        fetchData();
    }, [URL]);

    const handleClick = () => {
        fetchData();
        navigate(location.pathname);
    };

    return (
        <TableContainer component={Paper}>
            <Table aria-label='simple table'>
                <TableHead>
                    <TableRow>
                        <TableCell>{t('hot_water.date')}</TableCell>
                        <TableCell>
                            {t('hot_water.value') + ' [m3]'}
                        </TableCell>
                        <TableCell>
                            {t('hot_water.manager')}
                        </TableCell>
                        <TableCell>
                            {!hotWaterEntries.some(entry => {
                                return entry.date.includes(formattedDate)
                            }) ? <InsertHotWaterEntry placeId={placeId}/> : <div/>}
                        </TableCell>
                    </TableRow>
                </TableHead>
                <TableBody>
                    {hotWaterEntries.map((hotWaterEntry) => (
                        <TableRow key={hotWaterEntry.id}>
                            <TableCell>{hotWaterEntry.date}</TableCell>
                            <TableCell>{hotWaterEntry.entryValue}</TableCell>
                            <TableCell>{hotWaterEntry.manager}</TableCell>
                            <TableCell>{(hotWaterEntry.date.includes(formattedDate) && hotWaterEntry.manager === undefined)
                                && <ModifyHotWaterEntry hotWaterEntryId={hotWaterEntry.id} placeId={hotWaterEntry.placeId}/>}</TableCell>
                        </TableRow>
                    ))}
                    <TableRow><Button className="landing-page-button"
                                      onClick={handleClick}><RefreshIcon/></Button></TableRow>
                </TableBody>
            </Table>
        </TableContainer>
    );
}
export default HotWaterEntriesList;
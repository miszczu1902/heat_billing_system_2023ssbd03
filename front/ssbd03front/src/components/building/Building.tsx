import React, {useEffect, useState} from 'react';
import {
    Box,
    FormControl,
    FormHelperText,
    InputLabel,
    MenuItem,
    Paper,
    Select,
    SelectChangeEvent,
    Table,
    TableBody,
    TableCell,
    TableContainer,
    TableHead,
    TableRow,
    Typography
} from '@mui/material';
import {useTranslation} from "react-i18next";
import {useNavigate, useParams} from "react-router-dom";
import axios from 'axios';
import {API_URL} from '../../consts';
import {BuildingFromList} from '../../types/buildingFromList';
import {Place} from '../../types/place';
import IconButton from "@mui/material/IconButton";
import IconViewList from "../icons/IconViewList";
import Button from "@mui/material/Button";
import Dialog from "@mui/material/Dialog";
import DialogTitle from "@mui/material/DialogTitle";
import DialogContent from "@mui/material/DialogContent";
import List from "@mui/material/List";
import ListItem from "@mui/material/ListItem";
import TextField from "@mui/material/TextField";
import DialogActions from "@mui/material/DialogActions";
import Switch from '@mui/material/Switch';
import {Account} from "../../types/account";

const Building = () => {
    const {t, i18n} = useTranslation();
    const navigate = useNavigate();
    const token = 'Bearer ' + localStorage.getItem("token");
    const [version, setVersion] = useState("");
    const [building, setBuilding] = useState<BuildingFromList | null>(null);
    const buildingId = useParams().buildingId;
    const [places, setPlaces] = useState<Place[]>([]);
    const [addPlace, setAddPlace] = useState(false);

    const [area, setArea] = useState("");
    const [areaError, setAreaError] = useState("");
    const [areaValid, setAreaValid] = useState(false);

    const [hotWaterConnection, setHotWaterConnection] = useState(false);
    const [centralHeatingConnection, setCentralHeatingConnection] = useState(false);

    const [predictedHotWaterConsumption, setPredictedHotWaterConsumption] = useState("");
    const [predictedHotWaterConsumptionError, setPredictedHotWaterConsumptionError] = useState("");
    const [predictedHotWaterConsumptionValid, setPredictedHotWaterConsumptionValid] = useState(false);

    const [ownerId, setOwnerId] = useState("");
    const [ownerIdValid, setOwnerIdValid] = useState(false);

    const [dataError, setDataError] = useState("");
    const [confirmOpen, setConfirmOpen] = useState(false);
    const [errorOpen, setErrorOpen] = useState(false);
    const [successOpen, setSuccessOpen] = useState(false);
    const [accounts, setAccounts] = useState<Account[]>([]);

    const fetchData = async () => {
        axios.get(`${API_URL}/buildings/building/${buildingId}`, {
            headers: {
                Authorization: token
            }
        }).then(response => {
            setBuilding(response.data);
        }).catch(error => {
            if (error.response.status === 500) navigate('/');
        })

        axios.get(`${API_URL}/buildings/building/${buildingId}/places`, {
            headers: {
                Authorization: token
            }
        }).then(response => {
            setPlaces(response.data);
        }).catch(error => {
            if (error.response.status === 500) navigate('/');
        })
    };

    useEffect(() => {
        fetchData();
    }, [buildingId]);

    const goToPlace = (placeId: string) => {
        navigate('/places/place/' + placeId);
    }

    const handleClickOpen = () => {
        const fetchData = async () => {
            await axios.get(`${API_URL}/buildings/building/${buildingId}`, {
                headers: {
                    Authorization: token
                }
            })
                .then(response => {
                    localStorage.setItem("etag", response.headers["etag"]);
                    setVersion(response.data.version)
                });

            axios.get(`${API_URL}/buildings/owners`, {
                headers: {
                    Authorization: token
                }
            }).then(response => {
                setAccounts(response.data);
            });
        };
        fetchData();
        setAddPlace(true);
    };

    const handleClose = (event: React.SyntheticEvent<unknown>, reason?: string) => {
        if (reason !== 'backdropClick') {
            setAddPlace(false);
        }
    };

    const handleSumbit = (e: React.FormEvent<HTMLFormElement>) => {
        e.preventDefault();
    }

    const handleArea = (event: React.ChangeEvent<HTMLInputElement>) => {
        const area = event.target.value;
        setArea(area);

        const regex = /^\d+(\.\d{1,2})?$/;
        if (!regex.test(area) || parseFloat(area) < 0) {
            setAreaError(t('place.areaError'));
            setAreaValid(false);
        } else {
            setAreaError("");
            setAreaValid(true);
        }
    };

    const handlePredictedHotWaterConsumption = (event: React.ChangeEvent<HTMLInputElement>) => {
        const predictedHotWaterConsumption = event.target.value;
        setPredictedHotWaterConsumption(predictedHotWaterConsumption);

        const regex = /^\d+(\.\d{1,2})?$/;
        if (!regex.test(predictedHotWaterConsumption) || parseFloat(predictedHotWaterConsumption) < 0) {
            setPredictedHotWaterConsumptionError(t('place.predictedHotWaterConsumptionError'));
            setPredictedHotWaterConsumptionValid(false);
        } else {
            setPredictedHotWaterConsumptionError('');
            setPredictedHotWaterConsumptionValid(true);
        }
    };

    const handleConfirm = () => {
        if (areaValid && predictedHotWaterConsumptionValid && ownerId) {
            setDataError("");
            setConfirmOpen(true);
        } else {
            setDataError("");
        }
    }

    const handleConfirmClose = (event: React.SyntheticEvent<unknown>, reason?: string) => {
        if (reason !== 'backdropClick') {
            setConfirmOpen(false);
        }
    }

    const handleConfirmConfirm = (event: React.SyntheticEvent<unknown>, reason?: string) => {
        if (reason !== 'backdropClick') {
            setConfirmOpen(false);
        }
        const addPlaceDTO = {
            area: area,
            hotWaterConnection: hotWaterConnection,
            centralHeatingConnection: centralHeatingConnection,
            predictedHotWaterConsumption: predictedHotWaterConsumption,
            buildingId: buildingId,
            ownerId: ownerId,
            version: parseInt(version)
        }
        axios.post(`${API_URL}/buildings/place`,
            addPlaceDTO, {
                headers: {
                    Authorization: token,
                    'If-Match': localStorage.getItem("etag"),
                    'Content-Type': 'application/json'
                },
            })
            .then(response => {
                setSuccessOpen(true);
            })
            .catch(error => {
                setErrorOpen(true);
            });
        handleClose(event, reason);
    }


    const handleOwnerChange = (event: SelectChangeEvent<string>) => {
        setOwnerIdValid(true);
        setOwnerId(event.target.value);
    };

    const handleSuccessClose = (event: React.SyntheticEvent<unknown>, reason?: string) => {
        if (reason !== 'backdropClick') {
            setSuccessOpen(false);
        }
        window.location.reload();
    }

    const handleErrorClose = (event: React.SyntheticEvent<unknown>, reason?: string) => {
        if (reason !== 'backdropClick') {
            setArea("");
            setPredictedHotWaterConsumption("")
            setOwnerId("");
            setErrorOpen(false);
            setConfirmOpen(false);
        }
    };

    return (
        <div style={{height: '93.3vh', width: '100vw', boxSizing: 'border-box', left: 0, right: 0, bottom: 0}}>
            <Box sx={{width: '50%', maxWidth: '600px', margin: '2vh'}}>
                {building !== null && (
                    <Paper elevation={3} style={{padding: '2vh'}}>
                        <IconButton
                            edge="start"
                            color="inherit"
                            onClick={handleClickOpen}
                            aria-label="close"
                        >
                            <IconViewList/>
                        </IconButton>
                        <Typography variant="h5">
                            <b>{t('buildingFromList.created_by')}:</b> {building.createdBy}
                        </Typography>
                        <Typography variant="h5">
                            <b>{t('buildingFromList.total_area')}:</b> {building.totalArea}
                        </Typography>
                        <Typography variant="h5">
                            <b>{t('buildingFromList.communal_area_aggregate')}:</b> {building.communalAreaAggregate}
                        </Typography>
                        <Typography variant="h5">
                            <b>{t('buildingFromList.street')}:</b> {building.street}
                        </Typography>
                        <Typography variant="h5">
                            <b>{t('buildingFromList.building_number')}:</b> {building.buildingNumber}
                        </Typography>
                        <Typography variant="h5">
                            <b>{t('buildingFromList.city')}:</b> {building.city}
                        </Typography>
                        <Typography variant="h5">
                            <b>{t('buildingFromList.postal_code')}:</b> {building.postalCode}
                        </Typography>
                    </Paper>
                )}
            </Box>

            <Dialog disableEscapeKeyDown open={addPlace} onClose={handleClose}>
                <DialogTitle>{t('place.dialogText')}</DialogTitle>
                <DialogContent>
                    <Box sx={{display: 'flex', flexWrap: 'wrap'}}>
                        <form onSubmit={handleSumbit}>
                            <List component="nav" aria-label="mailbox folders">
                                <ListItem>
                                    <div className="form-group" onChange={handleArea}>
                                        <TextField
                                            id="outlined-helperText"
                                            label={t('place.area')}
                                            defaultValue={area}
                                            type="area"
                                            helperText={t('place.areaInfo')}
                                            error={Boolean(areaError)}
                                        />
                                        <div className={`form-group ${areaError ? 'error' : ''}`}> {areaError}
                                        </div>
                                    </div>
                                </ListItem>
                                <ListItem>
                                    <div className="form-group" onChange={handlePredictedHotWaterConsumption}>
                                        <TextField
                                            id="outlined-helperText"
                                            label={t('place.predictedHotWaterConsumption')}
                                            defaultValue={predictedHotWaterConsumption}
                                            type="predictedHotWaterConsumption"
                                            helperText={t('place.predictedHotWaterConsumptionInfo')}
                                            error={Boolean(predictedHotWaterConsumptionError)}
                                        />
                                        <div
                                            className={`form-group ${predictedHotWaterConsumptionError ? 'error' : ''}`}>
                                            {predictedHotWaterConsumptionError}
                                        </div>
                                    </div>
                                </ListItem>
                                <ListItem>
                                    <div className="form-group">
                                        <label htmlFor="hotWaterConnection">{t('place.hotWaterConnection')}</label>
                                        <Switch
                                            id="hotWaterConnection"
                                            checked={hotWaterConnection}
                                            onChange={(e) => setHotWaterConnection(e.target.checked)}
                                        />
                                    </div>
                                </ListItem>
                                <ListItem>
                                    <div className="form-group">
                                        <label
                                            htmlFor="centralHeatingConnection">{t('place.centralHeatingConnection')}</label>
                                        <Switch
                                            id="centralHeatingConnection"
                                            checked={centralHeatingConnection}
                                            onChange={(e) => setCentralHeatingConnection(e.target.checked)}
                                        />
                                    </div>
                                </ListItem>
                                <ListItem>
                                    <div className="form-group">
                                        <FormControl>
                                            <InputLabel id="owner-select-label">{t('place.ownerId')}</InputLabel>
                                            <Select
                                                labelId="owner-select-label"
                                                id="owner-select"
                                                value={ownerId}
                                                onChange={handleOwnerChange}
                                                fullWidth
                                            >
                                                {accounts.map((account) => (
                                                    <MenuItem key={account.id} value={account.id}>
                                                        {account.username}
                                                    </MenuItem>
                                                ))}
                                            </Select>
                                            <FormHelperText>{t('place.ownerIdInfo')}</FormHelperText>
                                        </FormControl>
                                    </div>
                                </ListItem>
                            </List>
                            <div className="form-group">
                                {dataError}
                            </div>
                        </form>
                    </Box>
                </DialogContent>
                <DialogActions>
                    <Button onClick={handleClose}>{t('confirm.cancel')}</Button>
                    <Button onClick={handleConfirm}
                            disabled={!(areaValid && predictedHotWaterConsumptionValid && ownerIdValid)}>
                        {t('profile.add')}
                    </Button>
                </DialogActions>
            </Dialog>

            <Dialog disableEscapeKeyDown open={confirmOpen}
                    onClose={handleConfirmClose}>
                <DialogTitle>{t('place.add_place')}</DialogTitle>
                <DialogActions>
                    <Button onClick={handleConfirmClose}>{t('confirm.no')}</Button>
                    <Button onClick={handleConfirmConfirm}>{t('confirm.yes')}</Button>
                </DialogActions>
            </Dialog>

            <Dialog disableEscapeKeyDown open={successOpen}>
                <DialogTitle>{t('place.added_place')}</DialogTitle>
                <Button onClick={handleSuccessClose}>{t('confirm.ok')}</Button>
            </Dialog>

            <Dialog disableEscapeKeyDown open={errorOpen}>
                <DialogTitle>{t('place.error')}</DialogTitle>
                <Button onClick={handleErrorClose}>{t('confirm.ok')}</Button>
            </Dialog>

            <TableContainer component={Paper}>
                <Table aria-label='simple table'>
                    <TableHead>
                        <TableRow>
                            <TableCell>
                                {t('place.created_by')}
                            </TableCell>
                            <TableCell>
                                {t('place.placeNumber')}
                            </TableCell>
                            <TableCell>
                                {t('place.area')}
                            </TableCell>
                            <TableCell>
                                {t('place.hotWaterConnection')}
                            </TableCell>
                            <TableCell>
                                {t('place.centralHeatingConnection')}
                            </TableCell>
                            <TableCell>
                                {t('place.predictedHotWaterConsumption')}
                            </TableCell>
                            <TableCell>
                                {t('place.firstName')}
                            </TableCell>
                            <TableCell>
                                {t('place.surname')}
                            </TableCell>
                            <TableCell></TableCell>
                        </TableRow>
                    </TableHead>
                    <TableBody>
                        {places.map((places) => (
                            <TableRow key={places.id}>
                                <TableCell>{places.createdBy}</TableCell>
                                <TableCell>{places.placeNumber}</TableCell>
                                <TableCell>{places.area}</TableCell>
                                <TableCell>
                                    {places.centralHeatingConnection ? t('place.yes') : t('place.no')}
                                </TableCell>
                                <TableCell>
                                    {places.predictedHotWaterConsumption ? t('place.yes') : t('place.no')}
                                </TableCell>
                                <TableCell>{places.predictedHotWaterConsumption}</TableCell>
                                <TableCell>{places.firstName}</TableCell>
                                <TableCell>{places.surname}</TableCell>
                                <TableCell>
                                    <IconButton
                                        edge="start"
                                        color="inherit"
                                        onClick={() => goToPlace(places.id.toString())}
                                        aria-label="close"
                                    >
                                        <IconViewList/>
                                    </IconButton>
                                </TableCell>
                            </TableRow>
                        ))}
                    </TableBody>
                </Table>
            </TableContainer>

        </div>
    );
}

export default Building;
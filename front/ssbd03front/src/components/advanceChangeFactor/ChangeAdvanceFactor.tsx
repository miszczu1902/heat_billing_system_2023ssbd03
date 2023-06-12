import {useLocation, useNavigate, useParams} from "react-router-dom";
import {useTranslation} from "react-i18next";
import React, {useEffect, useState} from "react";
import {API_URL} from "../../consts";
import {ActualAdvanceChangeFactor} from "../../types/ActualAdvanceChangeFactor";
import {
    Box,
    Paper,
    Typography
} from "@mui/material";
import IconButton from "@mui/material/IconButton";
import IconViewList from "../icons/IconViewList";
import Dialog from "@mui/material/Dialog";
import DialogTitle from "@mui/material/DialogTitle";
import DialogContent from "@mui/material/DialogContent";
import List from "@mui/material/List";
import ListItem from "@mui/material/ListItem";
import TextField from "@mui/material/TextField";
import DialogActions from "@mui/material/DialogActions";
import Button from "@mui/material/Button";
import axios from 'axios';


const ChangeAdvanceFactor = () => {
    const params = useParams();
    const location = useLocation();
    const {t, i18n} = useTranslation();
    const navigate = useNavigate();
    const token = 'Bearer ' + localStorage.getItem("token");
    const [advanceChangeFactor, setAdvanceChangeFactor] = useState<ActualAdvanceChangeFactor | null>(null);
    const [newChangeFactor, setNewChangeFactor] = useState('');
    const buildingId = params.buildingId;
    const URL  = `${API_URL}/heat-distribution-centre/parameters/advance-change-factor/${buildingId}`;
    const [modifyFactor, setModifyFactor]=  useState(false);
    const [factorValid, setFactorValid] = useState(false);
    const [factorError, setFactorError] = useState("");
    const [confirmOpen, setConfirmOpen] = useState(false);
    const [errorOpen, setErrorOpen] = useState(false);
    const [successOpen, setSuccessOpen] = useState(false);
    const [dataError, setDataError] = useState("");


    const fetchData = async () => {
        axios.get(URL, {
            headers: {
                Authorization: token
            }
        }).then(response => {
            setAdvanceChangeFactor(response.data);
            localStorage.setItem("etag", response.headers["etag"]);
        }).catch(error => {
            if (error.response.status == 403) navigate('/');
        })
    };

    useEffect(() => {
        fetchData();
    }, [URL]);

    const handleClickOpen = () => {
        setModifyFactor(true);
    };

    const handleClose = (event: React.SyntheticEvent<unknown>, reason?: string) => {
        if (reason !== 'backdropClick') {
            setModifyFactor(false);
        }
    };

    const handleSumbit = (e: React.FormEvent<HTMLFormElement>) => {
        e.preventDefault();
    }


    const handleNewFactorArea = (event: React.ChangeEvent<HTMLInputElement>) => {
        const newAreaFactorValue = event.target.value;
        setNewChangeFactor(newAreaFactorValue);

        console.log(newAreaFactorValue);
        const regex = /^[0-9]+(\.[0-9]{1,2})?$/;
        if (!regex.test(newAreaFactorValue) || parseFloat(newAreaFactorValue) < 0) {
            setFactorError(t('annual_balance.factor_invalid'));
            setFactorValid(false);
        } else {
            setFactorError('');
            setFactorValid(true);
        }
    };

    const handleConfirm = () => {
        if (factorValid && factorValid) {
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
        const modifyAdvanceChangeFactor = {
            advanceChangeFactor: newChangeFactor,
            version: advanceChangeFactor?.version
        };
        console.log(modifyAdvanceChangeFactor);
        axios.patch(`${API_URL}/heat-distribution-centre/parameters/advance-change-factor/${buildingId}`,
           modifyAdvanceChangeFactor, {
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


    const handleSuccessClose = (event: React.SyntheticEvent<unknown>, reason?: string) => {
        if (reason !== 'backdropClick') {
            setSuccessOpen(false);
        }
        fetchData();
        navigate(location.pathname);
    }

    const handleErrorClose = (event: React.SyntheticEvent<unknown>, reason?: string) => {
        if (reason !== 'backdropClick') {
            setNewChangeFactor('');
            setErrorOpen(false);
            setConfirmOpen(false);
        }
    };

    return (<div style={{width: '50vw', boxSizing: 'border-box', left: 0, bottom: 0}}>
        <Box sx={{width: '100%', maxWidth: '600px', margin: '2vh'}}>
            <Paper elevation={3} style={{padding: '2vh'}}>
                <Typography variant="h5">
                    <b>{t('annual_balance.change_factor')}: </b> {advanceChangeFactor?.advanceChangeFactor}
                </Typography>
                <IconButton
                    edge="start"
                    color="inherit"
                    onClick={handleClickOpen}
                    aria-label="close"
                >
                    <IconViewList/>
                </IconButton>
            </Paper>
    </Box>
        <Dialog disableEscapeKeyDown open={modifyFactor} onClose={handleClose}>
            <DialogTitle>{t('annual_balance.modify_change_factor')}</DialogTitle>
            <DialogContent>
                <Box sx={{display: 'flex', flexWrap: 'wrap'}}>
                    <form onSubmit={handleSumbit}>
                        <List component="nav" aria-label="mailbox folders">
                            <ListItem>
                                <div className="form-group" onChange={handleNewFactorArea}>
                                    <TextField
                                        id="outlined-helperText"
                                        label={t('annual_balance.factor_info')}
                                        defaultValue={advanceChangeFactor?.advanceChangeFactor}
                                        type="area"
                                        helperText={t('annual_balance.change_factor')}
                                        error={Boolean(factorError)}
                                    />
                                    <div className={`form-group ${factorError ? 'error' : ''}`}> {factorError}
                                    </div>
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
                        disabled={!factorValid}>
                    {t('profile.add')}
                </Button>
            </DialogActions>
        </Dialog>

        <Dialog disableEscapeKeyDown open={confirmOpen}
                onClose={handleConfirmClose}>
            <DialogTitle>{t('annual_balance.confirm_factor')}</DialogTitle>
            <DialogActions>
                <Button onClick={handleConfirmClose}>{t('confirm.no')}</Button>
                <Button onClick={handleConfirmConfirm}>{t('confirm.yes')}</Button>
            </DialogActions>
        </Dialog>

        <Dialog disableEscapeKeyDown open={successOpen}>
            <DialogTitle>{t('annual_balance.success_modified')}</DialogTitle>
            <Button onClick={handleSuccessClose}>{t('confirm.ok')}</Button>
        </Dialog>

        <Dialog disableEscapeKeyDown open={errorOpen}>
            <DialogTitle>{t('annual_balance.error')}</DialogTitle>
            <Button onClick={handleErrorClose}>{t('confirm.ok')}</Button>
        </Dialog>
    </div>)
}
export default ChangeAdvanceFactor;
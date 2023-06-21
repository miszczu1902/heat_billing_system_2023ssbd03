import {useTranslation} from "react-i18next";
import {useState} from "react";
import * as React from "react";
import axios from "axios";
import {API_URL, MANAGER} from "../../../consts";
import Button from "@mui/material/Button";
import Dialog from "@mui/material/Dialog";
import DialogTitle from "@mui/material/DialogTitle";
import DialogContent from "@mui/material/DialogContent";
import Box from "@mui/material/Box";
import List from "@mui/material/List";
import ListItem from "@mui/material/ListItem";
import {TextField, Snackbar, Alert} from "@mui/material";
import DialogActions from "@mui/material/DialogActions";

const ModifyHotWaterEntry: React.FC<{ hotWaterEntryId: number, placeId: number}> = ({ hotWaterEntryId, placeId }) => {
    const {t} = useTranslation();
    const token = "Bearer " + localStorage.getItem("token");
    const [version, setVersion] = useState("");
    const [open, setOpen] = useState(false);
    const [confirmOpen, setConfirmOpen] = useState(false);
    const [entryValue, setEntryValue] = useState("");
    const [entryError, setEntryError] = useState("");
    const [dataError, setDataError] = useState("");

    const [emailValid, setEmailValid] = useState(false);

    const [successOpen, setSuccessOpen] = useState(false);
    const [errorOpen, setErrorOpen] = useState(false);
    const [errorOpenMessage, setErrorOpenMessage] = useState("");
    const role = localStorage.getItem("role");
    const URL = role === MANAGER ? `${API_URL}/heat-distribution-centre/hot-water-consumption/${hotWaterEntryId}` : `${API_URL}/heat-distribution-centre/hot-water-consumption/owner/${hotWaterEntryId}`;

    const handleSumbit = (e: React.FormEvent<HTMLFormElement>) => {
        e.preventDefault();
    }

    const handleNewEmail = (event: React.ChangeEvent<HTMLInputElement>) => {
        let value = event.target.value;
        setEntryValue(value);
        if (parseFloat(value) <= 0) {
            setEntryError(t('hot_water.entry_error'));
            setEmailValid(false);
        } else {
            setEntryError("");
            setEmailValid(true);
        }
    };

    const handleClickOpen = () => {
        const fetchData = async () => {
            await axios.get(URL, {
                headers: {
                    Authorization: token
                }
            })
                .then(response => {
                    localStorage.setItem("etagHotWaterEntry", response.headers["etag"]);
                    setVersion(response.data.version);
                });
        };
        fetchData();
        setOpen(true);
    };

    const handleClose = (event: React.SyntheticEvent<unknown>, reason?: string) => {
        if (reason !== 'backdropClick') {
            setOpen(false);
        }
    };

    const handleConfirmClose = (event: React.SyntheticEvent<unknown>, reason?: string) => {
        if (reason !== 'backdropClick') {
            setConfirmOpen(false);
        }
    }

    const handleConfirmConfirm = (event: React.SyntheticEvent<unknown>, reason?: string) => {
        if (reason !== 'backdropClick') {
            setConfirmOpen(false);
        }
        const modifyHotWaterEntry = {
            hotWaterConsumption: entryValue,
            placeId: placeId,
            version: parseInt(version)
        }
        const fetchData = async () => {
            await axios.patch(`${API_URL}/heat-distribution-centre/parameters/insert-consumption`,
                modifyHotWaterEntry, {
                    headers: {
                        'Authorization': token,
                        'If-Match': localStorage.getItem("etagHotWaterEntry"),
                        'Content-Type': 'application/json'
                    },
                })
                .then(response => {
                    setSuccessOpen(true);
                })
                .catch(error => {
                    setErrorOpenMessage(t(error.response.data.message))
                    setErrorOpen(true);
                });
        };
        fetchData();
        handleClose(event, reason);
    }

    const handleConfirm = () => {
        if (emailValid) {
            setDataError("");
            setConfirmOpen(true);
        } else {
            setDataError(t('hot_water.entry_error'));
        }
    }

    const handleSuccessClose = () => {
        setEntryValue("");
        setSuccessOpen(false);
        window.location.reload();
    }

    const handleErrorClose = () => {
        setErrorOpen(false);
    };

    return (
        <div>
            <div>
                <Button onClick={handleClickOpen} variant="contained">{t('hot_water.enter')}</Button>
            </div>
            <Dialog disableEscapeKeyDown open={open} onClose={handleClose}>
                <DialogTitle>{t('hot_water.enter')}</DialogTitle>
                <DialogContent>
                    <Box sx={{display: 'flex', flexWrap: 'wrap'}}>
                        <form onSubmit={handleSumbit}>
                            <List component="nav" aria-label="mailbox folders">
                                <ListItem>
                                    <div className="form-group" onChange={handleNewEmail}>
                                        <TextField
                                            id="outlined-helperText"
                                            label={t('hot_water.value') + " [m3]*"}
                                            defaultValue={entryValue}
                                            type="email"
                                            helperText={t('hot_water.entry_not_correct')}
                                        />
                                        <div className="form-group">
                                            {entryError}
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
                    <Button onClick={handleConfirm} disabled={!emailValid}>{t('confirm.ok')}</Button>
                </DialogActions>
            </Dialog>

            <Dialog disableEscapeKeyDown open={confirmOpen} onClose={handleConfirmClose}>
                <DialogTitle>{t('hot_water.confirm_changes')}</DialogTitle>
                <DialogActions>
                    <Button onClick={handleConfirmClose}>{t('confirm.no')}</Button>
                    <Button onClick={handleConfirmConfirm}>{t('confirm.yes')}</Button>
                </DialogActions>
            </Dialog>

            <Snackbar open={successOpen} autoHideDuration={6000} onClose={handleSuccessClose}>
                <Alert onClose={handleSuccessClose} severity="success" sx={{width: '100%'}}>
                    {t('hot_water.success_title')}
                </Alert>
            </Snackbar>

            <Snackbar open={errorOpen} autoHideDuration={6000} onClose={handleErrorClose}>
                <Alert onClose={handleErrorClose} severity="error" sx={{width: '100%'}}>
                {t(errorOpenMessage)}
                </Alert>
            </Snackbar>
        </div>
    );
}
export default ModifyHotWaterEntry;
import {useTranslation} from "react-i18next";
import {useState} from "react";
import * as React from "react";
import axios from "axios";
import {API_URL} from "../../../consts";
import Button from "@mui/material/Button";
import Dialog from "@mui/material/Dialog";
import DialogTitle from "@mui/material/DialogTitle";
import DialogContent from "@mui/material/DialogContent";
import Box from "@mui/material/Box";
import List from "@mui/material/List";
import ListItem from "@mui/material/ListItem";
import {TextField, Snackbar, Alert} from "@mui/material";
import DialogActions from "@mui/material/DialogActions";

const InsertHotWaterEntry: React.FC<{placeId: any}> = ({placeId}) => {
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

    const handleSumbit = (e: React.FormEvent<HTMLFormElement>) => {
        e.preventDefault();
    }

    const handleNewEmail = (event: React.ChangeEvent<HTMLInputElement>) => {
        let value = event.target.value;
        setEntryValue(value);
        if (parseFloat(value) <= 0) {
            setEntryError(t('hot_water.entry_not_correct'));
            setEmailValid(false);
        } else {
            setEntryError("");
            setEmailValid(true);
        }
    };

    const handleClickOpen = () => {
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
        const insertHotWaterEntry = {
            hotWaterConsumption: entryValue,
            placeId: placeId
        }
        const fetchData = async () => {
            await axios.post(`${API_URL}/heat-distribution-centre/parameters/insert-consumption`,
                insertHotWaterEntry, {
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
                    setErrorOpenMessage(t('hot_water.failure_title'))
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
                <Button onClick={handleClickOpen} variant="contained">{t('hot_water.add')}</Button>
            </div>
            <Dialog disableEscapeKeyDown open={open} onClose={handleClose}>
                <DialogTitle>{t('hot_water.add')}</DialogTitle>
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
                <DialogTitle>{t('hot_water.confirm_add')}</DialogTitle>
                <DialogActions>
                    <Button onClick={handleConfirmClose}>{t('confirm.no')}</Button>
                    <Button onClick={handleConfirmConfirm}>{t('confirm.yes')}</Button>
                </DialogActions>
            </Dialog>

            <Snackbar open={successOpen} autoHideDuration={6000} onClose={handleSuccessClose}>
                <Alert onClose={handleSuccessClose} severity="success" sx={{width: '100%'}}>
                    {t('hot_water.added')}
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
export default InsertHotWaterEntry;
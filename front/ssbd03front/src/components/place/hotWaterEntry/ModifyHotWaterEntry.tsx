import {useTranslation} from "react-i18next";
import {useState} from "react";
import {useLocation, useNavigate, useParams} from "react-router-dom";
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
import {TextField} from "@mui/material";
import DialogActions from "@mui/material/DialogActions";

const ModifyHotWaterEntry: React.FC<{ hotWaterEntryId: number, placeId: number}> = ({ hotWaterEntryId, placeId }) => {
    const {t} = useTranslation();
    const location = useLocation();
    const navigate = useNavigate();
    const token = "Bearer " + localStorage.getItem("token");
    const [version, setVersion] = useState("");
    const [open, setOpen] = useState(false);
    const [confirmOpen, setConfirmOpen] = useState(false);
    const [entryValue, setEntryValue] = useState("");
    const [emailError, setEmailError] = useState("");
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
            setEmailError(t('register.email_error'));
            setEmailValid(false);
        } else {
            setEmailError("");
            setEmailValid(true);
        }
    };

    const handleClickOpen = () => {
        const fetchData = async () => {
            await axios.get(`${API_URL}/heat-distribution-centre/hot-water-consumption/${hotWaterEntryId}`, {
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
            setDataError(t('register.email_error'));
        }
    }

    const handleSuccessClose = (event: React.SyntheticEvent<unknown>, reason?: string) => {
        setEntryValue("");
        if (reason !== 'backdropClick') {
            setSuccessOpen(false);
        }
        window.location.reload();
    }

    const handleErrorClose = (event: React.SyntheticEvent<unknown>, reason?: string) => {
        if (reason !== 'backdropClick') {
            setErrorOpen(false);
        }
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
                                            label={t('hot_water.value') + " [m3]"}
                                            defaultValue={entryValue}
                                            type="email"
                                            helperText={t('hot_water.entry_not_correct')}
                                        />
                                        <div className="form-group">
                                            {emailError}
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

            <Dialog disableEscapeKeyDown open={successOpen}>
                <DialogTitle>{t('hot_water.success_title')}</DialogTitle>
                <Button onClick={handleSuccessClose}>{t('confirm.ok')}</Button>
            </Dialog>

            <Dialog disableEscapeKeyDown open={errorOpen}>
                <DialogTitle>{t(errorOpenMessage)}</DialogTitle>
                <Button onClick={handleErrorClose}>{t('confirm.ok')}</Button>
            </Dialog>
        </div>
    );
}
export default ModifyHotWaterEntry;
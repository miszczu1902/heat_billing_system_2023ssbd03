export interface Place {
    id: number;
    version: number;
    placeNumber: number;
    area: number;
    hotWaterConnection: boolean;
    centralHeatingConnection: boolean;
    predictedHotWaterConsumption: number;
    street: string;
    buildingNumber: string;
    city: string;
    postalCode: string;
}
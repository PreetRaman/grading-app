import { IUser } from 'app/core/user/user.model';

export interface IFdaiNummer {
    id?: number;
    fdainumber?: string;
    ip?: number;
    user?: IUser;
}

export class FdaiNummer implements IFdaiNummer {
    constructor(public id?: number, public fdainumber?: string, public ip?: number, public user?: IUser) {}
}

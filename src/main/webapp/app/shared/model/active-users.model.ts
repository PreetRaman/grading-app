import { Moment } from 'moment';

export interface IActiveUsers {
    id?: number;
    username?: string;
    login_time?: Moment;
    logout_time?: Moment;
    active?: boolean;
}

export class ActiveUsers implements IActiveUsers {
    constructor(
        public id?: number,
        public username?: string,
        public login_time?: Moment,
        public logout_time?: Moment,
        public active?: boolean
    ) {
        this.active = this.active || false;
    }
}

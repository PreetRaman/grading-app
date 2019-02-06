import { Moment } from 'moment';

export interface IActiveUsers {
    id?: number;
    username?: string;
    login_time?: Moment;
    logout_time?: Moment;
    active?: boolean;
    is_ip_address?: string;
    should_ip_address?: string;
}

export class ActiveUsers implements IActiveUsers {
    constructor(
        public id?: number,
        public username?: string,
        public login_time?: Moment,
        public logout_time?: Moment,
        public active?: boolean,
        public is_ip_address?: string,
        public should_ip_address?: string
    ) {
        this.active = this.active || false;
    }
}

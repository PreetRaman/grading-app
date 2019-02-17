import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';

import { GradingAppSharedModule } from 'app/shared';
import { HOME_ROUTE, HomeComponent } from './';
import {GradingAppActiveUsersModule} from '../entities/active-users/active-users.module';

@NgModule({
    imports: [GradingAppSharedModule, RouterModule.forChild([HOME_ROUTE], GradingAppActiveUsersModule)],
    declarations: [HomeComponent],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class GradingAppHomeModule {}

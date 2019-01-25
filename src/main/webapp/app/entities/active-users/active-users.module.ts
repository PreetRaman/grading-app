import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';

import { GradingAppSharedModule } from 'app/shared';
import {
    ActiveUsersComponent,
    ActiveUsersDetailComponent,
    ActiveUsersUpdateComponent,
    ActiveUsersDeletePopupComponent,
    ActiveUsersDeleteDialogComponent,
    activeUsersRoute,
    activeUsersPopupRoute
} from './';

const ENTITY_STATES = [...activeUsersRoute, ...activeUsersPopupRoute];

@NgModule({
    imports: [GradingAppSharedModule, RouterModule.forChild(ENTITY_STATES)],
    declarations: [
        ActiveUsersComponent,
        ActiveUsersDetailComponent,
        ActiveUsersUpdateComponent,
        ActiveUsersDeleteDialogComponent,
        ActiveUsersDeletePopupComponent
    ],
    entryComponents: [ActiveUsersComponent, ActiveUsersUpdateComponent, ActiveUsersDeleteDialogComponent, ActiveUsersDeletePopupComponent],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class GradingAppActiveUsersModule {}

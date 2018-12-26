import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';

import { GradingAppSharedModule } from 'app/shared';
import { GradingAppAdminModule } from 'app/admin/admin.module';
import {
    FdaiNummerComponent,
    FdaiNummerDetailComponent,
    FdaiNummerUpdateComponent,
    FdaiNummerDeletePopupComponent,
    FdaiNummerDeleteDialogComponent,
    fdaiNummerRoute,
    fdaiNummerPopupRoute
} from './';

const ENTITY_STATES = [...fdaiNummerRoute, ...fdaiNummerPopupRoute];

@NgModule({
    imports: [GradingAppSharedModule, GradingAppAdminModule, RouterModule.forChild(ENTITY_STATES)],
    declarations: [
        FdaiNummerComponent,
        FdaiNummerDetailComponent,
        FdaiNummerUpdateComponent,
        FdaiNummerDeleteDialogComponent,
        FdaiNummerDeletePopupComponent
    ],
    entryComponents: [FdaiNummerComponent, FdaiNummerUpdateComponent, FdaiNummerDeleteDialogComponent, FdaiNummerDeletePopupComponent],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class GradingAppFdaiNummerModule {}

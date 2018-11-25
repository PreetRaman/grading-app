import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';

import { GradingAppSharedModule } from 'app/shared';
import { GradingAppAdminModule } from 'app/admin/admin.module';
import {
    SubmissionsComponent,
    SubmissionsDetailComponent,
    SubmissionsUpdateComponent,
    SubmissionsDeletePopupComponent,
    SubmissionsDeleteDialogComponent,
    submissionsRoute,
    submissionsPopupRoute
} from './';

const ENTITY_STATES = [...submissionsRoute, ...submissionsPopupRoute];

@NgModule({
    imports: [GradingAppSharedModule, GradingAppAdminModule, RouterModule.forChild(ENTITY_STATES)],
    declarations: [
        SubmissionsComponent,
        SubmissionsDetailComponent,
        SubmissionsUpdateComponent,
        SubmissionsDeleteDialogComponent,
        SubmissionsDeletePopupComponent
    ],
    entryComponents: [SubmissionsComponent, SubmissionsUpdateComponent, SubmissionsDeleteDialogComponent, SubmissionsDeletePopupComponent],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class GradingAppSubmissionsModule {}

import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';

import { GradingAppSubmissionsModule } from './submissions/submissions.module';
import { GradingAppFdaiNummerModule } from './fdai-nummer/fdai-nummer.module';
import { GradingAppActiveUsersModule } from './active-users/active-users.module';
/* jhipster-needle-add-entity-module-import - JHipster will add entity modules imports here */

@NgModule({
    // prettier-ignore
    imports: [
        GradingAppSubmissionsModule,
        GradingAppFdaiNummerModule,
        GradingAppActiveUsersModule,
        /* jhipster-needle-add-entity-module - JHipster will add entity modules here */
    ],
    declarations: [],
    entryComponents: [],
    providers: [],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class GradingAppEntityModule {}

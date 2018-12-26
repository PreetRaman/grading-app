import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, RouterStateSnapshot, Routes } from '@angular/router';
import { UserRouteAccessService } from 'app/core';
import { Observable, of } from 'rxjs';
import { filter, map } from 'rxjs/operators';
import { FdaiNummer } from 'app/shared/model/fdai-nummer.model';
import { FdaiNummerService } from './fdai-nummer.service';
import { FdaiNummerComponent } from './fdai-nummer.component';
import { FdaiNummerDetailComponent } from './fdai-nummer-detail.component';
import { FdaiNummerUpdateComponent } from './fdai-nummer-update.component';
import { FdaiNummerDeletePopupComponent } from './fdai-nummer-delete-dialog.component';
import { IFdaiNummer } from 'app/shared/model/fdai-nummer.model';

@Injectable({ providedIn: 'root' })
export class FdaiNummerResolve implements Resolve<IFdaiNummer> {
    constructor(private service: FdaiNummerService) {}

    resolve(route: ActivatedRouteSnapshot, state: RouterStateSnapshot): Observable<FdaiNummer> {
        const id = route.params['id'] ? route.params['id'] : null;
        if (id) {
            return this.service.find(id).pipe(
                filter((response: HttpResponse<FdaiNummer>) => response.ok),
                map((fdaiNummer: HttpResponse<FdaiNummer>) => fdaiNummer.body)
            );
        }
        return of(new FdaiNummer());
    }
}

export const fdaiNummerRoute: Routes = [
    {
        path: 'fdai-nummer',
        component: FdaiNummerComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'gradingApp.fdaiNummer.home.title'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: 'fdai-nummer/:id/view',
        component: FdaiNummerDetailComponent,
        resolve: {
            fdaiNummer: FdaiNummerResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'gradingApp.fdaiNummer.home.title'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: 'fdai-nummer/new',
        component: FdaiNummerUpdateComponent,
        resolve: {
            fdaiNummer: FdaiNummerResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'gradingApp.fdaiNummer.home.title'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: 'fdai-nummer/:id/edit',
        component: FdaiNummerUpdateComponent,
        resolve: {
            fdaiNummer: FdaiNummerResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'gradingApp.fdaiNummer.home.title'
        },
        canActivate: [UserRouteAccessService]
    }
];

export const fdaiNummerPopupRoute: Routes = [
    {
        path: 'fdai-nummer/:id/delete',
        component: FdaiNummerDeletePopupComponent,
        resolve: {
            fdaiNummer: FdaiNummerResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'gradingApp.fdaiNummer.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    }
];

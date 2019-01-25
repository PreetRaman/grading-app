import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, RouterStateSnapshot, Routes } from '@angular/router';
import { JhiPaginationUtil, JhiResolvePagingParams } from 'ng-jhipster';
import { UserRouteAccessService } from 'app/core';
import { Observable, of } from 'rxjs';
import { filter, map } from 'rxjs/operators';
import { ActiveUsers } from 'app/shared/model/active-users.model';
import { ActiveUsersService } from './active-users.service';
import { ActiveUsersComponent } from './active-users.component';
import { ActiveUsersDetailComponent } from './active-users-detail.component';
import { ActiveUsersUpdateComponent } from './active-users-update.component';
import { ActiveUsersDeletePopupComponent } from './active-users-delete-dialog.component';
import { IActiveUsers } from 'app/shared/model/active-users.model';

@Injectable({ providedIn: 'root' })
export class ActiveUsersResolve implements Resolve<IActiveUsers> {
    constructor(private service: ActiveUsersService) {}

    resolve(route: ActivatedRouteSnapshot, state: RouterStateSnapshot): Observable<ActiveUsers> {
        const id = route.params['id'] ? route.params['id'] : null;
        if (id) {
            return this.service.find(id).pipe(
                filter((response: HttpResponse<ActiveUsers>) => response.ok),
                map((activeUsers: HttpResponse<ActiveUsers>) => activeUsers.body)
            );
        }
        return of(new ActiveUsers());
    }
}

export const activeUsersRoute: Routes = [
    {
        path: 'active-users',
        component: ActiveUsersComponent,
        resolve: {
            pagingParams: JhiResolvePagingParams
        },
        data: {
            authorities: ['ROLE_USER'],
            defaultSort: 'id,asc',
            pageTitle: 'gradingApp.activeUsers.home.title'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: 'active-users/:id/view',
        component: ActiveUsersDetailComponent,
        resolve: {
            activeUsers: ActiveUsersResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'gradingApp.activeUsers.home.title'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: 'active-users/new',
        component: ActiveUsersUpdateComponent,
        resolve: {
            activeUsers: ActiveUsersResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'gradingApp.activeUsers.home.title'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: 'active-users/:id/edit',
        component: ActiveUsersUpdateComponent,
        resolve: {
            activeUsers: ActiveUsersResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'gradingApp.activeUsers.home.title'
        },
        canActivate: [UserRouteAccessService]
    }
];

export const activeUsersPopupRoute: Routes = [
    {
        path: 'active-users/:id/delete',
        component: ActiveUsersDeletePopupComponent,
        resolve: {
            activeUsers: ActiveUsersResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'gradingApp.activeUsers.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    }
];

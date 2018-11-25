import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, RouterStateSnapshot, Routes } from '@angular/router';
import { UserRouteAccessService } from 'app/core';
import { Observable, of } from 'rxjs';
import { filter, map } from 'rxjs/operators';
import { Submissions } from 'app/shared/model/submissions.model';
import { SubmissionsService } from './submissions.service';
import { SubmissionsComponent } from './submissions.component';
import { SubmissionsDetailComponent } from './submissions-detail.component';
import { SubmissionsUpdateComponent } from './submissions-update.component';
import { SubmissionsDeletePopupComponent } from './submissions-delete-dialog.component';
import { ISubmissions } from 'app/shared/model/submissions.model';

@Injectable({ providedIn: 'root' })
export class SubmissionsResolve implements Resolve<ISubmissions> {
    constructor(private service: SubmissionsService) {}

    resolve(route: ActivatedRouteSnapshot, state: RouterStateSnapshot): Observable<Submissions> {
        const id = route.params['id'] ? route.params['id'] : null;
        if (id) {
            return this.service.find(id).pipe(
                filter((response: HttpResponse<Submissions>) => response.ok),
                map((submissions: HttpResponse<Submissions>) => submissions.body)
            );
        }
        return of(new Submissions());
    }
}

export const submissionsRoute: Routes = [
    {
        path: 'submissions',
        component: SubmissionsComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'gradingApp.submissions.home.title'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: 'submissions/:id/view',
        component: SubmissionsDetailComponent,
        resolve: {
            submissions: SubmissionsResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'gradingApp.submissions.home.title'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: 'submissions/new',
        component: SubmissionsUpdateComponent,
        resolve: {
            submissions: SubmissionsResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'gradingApp.submissions.home.title'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: 'submissions/:id/edit',
        component: SubmissionsUpdateComponent,
        resolve: {
            submissions: SubmissionsResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'gradingApp.submissions.home.title'
        },
        canActivate: [UserRouteAccessService]
    }
];

export const submissionsPopupRoute: Routes = [
    {
        path: 'submissions/:id/delete',
        component: SubmissionsDeletePopupComponent,
        resolve: {
            submissions: SubmissionsResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'gradingApp.submissions.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    }
];
